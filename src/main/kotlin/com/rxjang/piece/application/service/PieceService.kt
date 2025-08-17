package com.rxjang.piece.application.service

import com.rxjang.piece.application.common.PieceFailureCode
import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.dto.ChangeProblemOrderFailure
import com.rxjang.piece.application.dto.ChangeProblemOrderResult
import com.rxjang.piece.application.dto.ChangeProblemOrderSuccess
import com.rxjang.piece.application.dto.CreatePieceFailure
import com.rxjang.piece.application.dto.CreatePieceResult
import com.rxjang.piece.application.dto.CreatePieceSuccess
import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.command.ChangeProblemOrderCommand
import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.reader.PieceReader
import com.rxjang.piece.domain.piece.store.PieceStore
import com.rxjang.piece.domain.problem.reader.ProblemReader
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class PieceService(
    private val problemReader: ProblemReader,
    private val pieceReader: PieceReader,
    private val pieceStore: PieceStore,
) {

    @Transactional
    fun createPiece(command: CreatePieceCommand): CreatePieceResult {
        // 문제 정렬해서 가져오기
        val problems = problemReader.getOrderedProblemsByIds(command.problemIds.toList())
        if (problems.size != command.problemIds.size) {
            return CreatePieceFailure(PieceFailureCode.SOME_PROBLEMS_NOT_EXIST)
        }
        // 정렬된 순서로 학습지 생성
        val piece = pieceStore.createPiece(command.copy(problemIds = problems.map { it.id }.toSet()))
        return CreatePieceSuccess(piece)
    }

    @Transactional
    fun changeProblemOrder(command: ChangeProblemOrderCommand): ChangeProblemOrderResult {
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
        if (piece.teacherId != command.teacherId) {
            return AssignPieceResult.Failure(PieceFailureCode.NOT_OWN_PIECE)
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
}