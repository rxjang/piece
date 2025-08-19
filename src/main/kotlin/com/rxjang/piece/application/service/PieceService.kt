package com.rxjang.piece.application.service

import com.rxjang.piece.application.exception.codes.PieceFailureCode
import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.dto.ChangeProblemOrderFailure
import com.rxjang.piece.application.dto.ChangeProblemOrderResult
import com.rxjang.piece.application.dto.ChangeProblemOrderSuccess
import com.rxjang.piece.application.dto.CreatePieceFailure
import com.rxjang.piece.application.dto.CreatePieceResult
import com.rxjang.piece.application.dto.CreatePieceSuccess
import com.rxjang.piece.application.dto.GetPieceStaticsResult
import com.rxjang.piece.domain.piece.model.PieceStatistics
import com.rxjang.piece.domain.piece.command.SaveScoredAnswerCommand
import com.rxjang.piece.application.dto.ScorePieceResult
import com.rxjang.piece.application.dto.converter.PieceConverter.toCommand
import com.rxjang.piece.application.dto.request.ChangeProblemOrderInPieceRequest
import com.rxjang.piece.application.dto.request.CreatePieceRequest
import com.rxjang.piece.application.dto.request.ScorePieceRequest
import com.rxjang.piece.domain.piece.model.StudentStatistic
import com.rxjang.piece.domain.piece.command.UpdatePieceAssignmentCommand
import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.model.AssignmentStatus
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.reader.PieceReader
import com.rxjang.piece.domain.piece.store.PieceStore
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.reader.ProblemReader
import com.rxjang.piece.domain.auth.service.AuthenticationContext
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class PieceService(
    private val problemReader: ProblemReader,
    private val pieceReader: PieceReader,
    private val pieceStore: PieceStore,
    private val authContext: AuthenticationContext,
) {

    @Transactional(readOnly = true)
    fun findProblemsInPieceForStudent(pieceId: Int): List<Problem> {
        return pieceReader.findProblemsInPieceForStudent(PieceId(pieceId), authContext.getCurrentStudentId())
    }

    @Transactional
    fun createPiece(request: CreatePieceRequest): CreatePieceResult {
        // 문제 정렬해서 가져오기
        val command = request.toCommand()
        val problems = problemReader.getOrderedProblemsByIds(command.problemIds.toList())
        if (problems.size != command.problemIds.size) {
            return CreatePieceFailure(PieceFailureCode.SOME_PROBLEMS_NOT_EXIST)
        }
        // 정렬된 순서로 학습지 생성
        val piece = pieceStore.createPiece(command.copy(problemIds = problems.map { it.id }.toSet()), authContext.getCurrentTeacherId())
        return CreatePieceSuccess(piece)
    }

    @Transactional
    fun changeProblemOrder(pieceId: Int, request: ChangeProblemOrderInPieceRequest): ChangeProblemOrderResult {
        val command = request.toCommand(pieceId)
        // command 순서 검증
        if (!command.validateOrder()) {
            return ChangeProblemOrderFailure(PieceFailureCode.INVALID_PROBLEM_ORDER)
        }
        // 기존 학습지 가져오기
        val piece = pieceReader.findById(command.pieceId) ?: return ChangeProblemOrderFailure(PieceFailureCode.PIECE_NOT_FOUND)

        // 기존 학습지에 매핑되어있는 문제만 존재하는지 확인
        val requestProblemIds = command.problemOrders.map { it.problemId }
        logger.debug { "[학습지 문제 순서 조정] requestIds: $requestProblemIds, actualIds: ${piece.problemIds}" }
        if (!piece.problemIds.containsAll(requestProblemIds)) {
            return ChangeProblemOrderFailure(PieceFailureCode.PROBLEM_ID_NOT_MATCHED_FOR_ORDER)
        }
        val updated = pieceStore.changeProblemOrder(command)
        return ChangeProblemOrderSuccess(piece.copy(problemIds = updated))
    }

    @Transactional
    fun assignPieceToStudent(command: AssignPieceCommand): AssignPieceResult {
        // 기존 학습지 가져오기
        val piece = pieceReader.findById(command.pieceId) ?: return AssignPieceResult.Failure(PieceFailureCode.PIECE_NOT_FOUND)
        if (piece.teacherId != authContext.getCurrentTeacherId()) {
            return AssignPieceResult.Failure(PieceFailureCode.UNAUTHORIZED_ACCESS)
        }
        // 기 출제된 학생 제외
        val alreadyAssignedStudents = pieceReader.findAlreadyAssignedStudents(command)
        if (alreadyAssignedStudents.size == command.studentIds.size) {
            return AssignPieceResult.Success(emptyList())
        }
        // 나머지 학생들에게 학습지 출제
        val assignments = pieceStore.assignToStudent(command.copy(studentIds = command.studentIds - alreadyAssignedStudents))
        return AssignPieceResult.Success(assignments)
    }

    @Transactional
    fun score(pieceId: Int, request: ScorePieceRequest): ScorePieceResult {
        val studentId = authContext.getCurrentStudentId()
        val command = request.toCommand(pieceId)
        // 학생에게 받은 학습지가 맞는지 확인
        val assignment = pieceReader.findPieceAssignment(command.pieceId, studentId)
            ?: return ScorePieceResult.Failure(PieceFailureCode.ASSIGNMENT_NOT_FOUND)

        // 이미 완료된 학습지인지 확인
        if (assignment.status == AssignmentStatus.COMPLETED) {
            return ScorePieceResult.Failure(PieceFailureCode.ALREADY_COMPLETED)
        }

        // 해당 학습지의 모든 문제 조회
        val pieceProblems = pieceReader.findProblemsInPieceForStudent(command.pieceId, studentId)
        // 모든 문제가 답변으로 들어왔는지 확인
        val problemIds = pieceProblems.map { it.id }.toSet()
        val submittedProblemIds = command.answers.map { it.problemId }.toSet()
        if (problemIds != submittedProblemIds) {
            return ScorePieceResult.Failure(PieceFailureCode.ANSWER_MISMATCH)
        }

        // 답변 비교 및 채점
        val correctAnswerMap = pieceProblems.associate { problem ->
            problem.id to problem.answer
        }
        val scoringResults = command.answers.map { submission ->
            val correctAnswer = correctAnswerMap[submission.problemId]
                ?: throw IllegalStateException("문제를 찾을 수 없습니다: ${submission.problemId}")

            val isCorrect = isAnswerCorrect(submission.answer, correctAnswer)
            val score = if (isCorrect) 1 else 0

            SaveScoredAnswerCommand(
                pieceAssignmentId = assignment.id,
                problemId = submission.problemId,
                studentAnswer = submission.answer,
                correctAnswer = correctAnswer,
                isCorrect = isCorrect,
                score = score
            )
        }

        // 채점 결과 저장
        pieceStore.saveScoringPieceResult(scoringResults)

        // 채점 결과 및 문제 풀이 완료 저장
        val totalScore = scoringResults.sumOf { it.score }
        val updateAssignmentCommand = UpdatePieceAssignmentCommand(
            pieceAssignmentId = assignment.id,
            score = totalScore,
        )
        pieceStore.updateAssignment(updateAssignmentCommand)
        // 9. 결과 반환
        return ScorePieceResult.Success(score = totalScore)
    }

    /**
     * 답변 정확성 검사
     * 추후 더 정교한 로직으로 확장 가능 (유사도 검사, 키워드 매칭 등)
     */
    private fun isAnswerCorrect(studentAnswer: String, correctAnswer: String): Boolean {
        return studentAnswer.trim().lowercase() == correctAnswer.trim().lowercase()
    }

    @Transactional(readOnly = true)
    fun getPieceStatistics(pieceId: Int): GetPieceStaticsResult {
        val pieceId = PieceId(pieceId)
        // 선생님이 만든 학습지인지 확인
        val piece = pieceReader.findById(pieceId) ?: return GetPieceStaticsResult.Failure(PieceFailureCode.PIECE_NOT_FOUND)
        if (piece.teacherId != authContext.getCurrentTeacherId()) {
            return GetPieceStaticsResult.Failure(PieceFailureCode.UNAUTHORIZED_ACCESS)
        }

        // 학습지에 출제된 모든 assignment 조회
        val assignments = pieceReader.findPieceAssignments(pieceId)

        // 아직 아무에게도 출제하지 않은 학습지
        if (assignments.isEmpty()) {
            return GetPieceStaticsResult.Success(
                PieceStatistics(
                    pieceId = pieceId,
                    title = piece.title,
                )
            )
        }

        // 학생별 통계 계산
        val studentStatistics = assignments.map { assignment ->
            val correctRate = (assignment.score.toDouble() / piece.problemIds.size) * 100
            StudentStatistic(
                studentId = assignment.studentId,
                status = assignment.status,
                score = assignment.score,
                correctRate = correctRate,
            )
        }

        // 문제별 통계 계산
        val problemStatistics = pieceReader.getProblemStatisticsByPieceId(pieceId)

        // 전체 통계 계산
        val completedAssignments = assignments.count { it.status == AssignmentStatus.COMPLETED }
        val averageScore = studentStatistics
            .map { it.correctRate }
            .takeIf { it.isNotEmpty() }
            ?.average() ?: 0.0

        return GetPieceStaticsResult.Success(
                PieceStatistics(
                pieceId = pieceId,
                title = piece.title,
                totalAssignments = assignments.size,
                completedAssignments = completedAssignments,
                averageScore = averageScore,
                studentStatistics = studentStatistics,
                problemStatistics = problemStatistics
            )
        )
    }

}