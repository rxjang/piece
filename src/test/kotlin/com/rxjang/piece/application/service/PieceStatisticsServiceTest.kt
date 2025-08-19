package com.rxjang.piece.application.service

import com.rxjang.piece.application.dto.GetPieceStaticsResult
import com.rxjang.piece.application.exception.codes.PieceFailureCode
import com.rxjang.piece.domain.auth.service.AuthenticationContext
import com.rxjang.piece.domain.piece.model.AssignmentStatus
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceAssignment
import com.rxjang.piece.domain.piece.model.PieceAssignmentId
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.model.ProblemStatistic
import com.rxjang.piece.domain.piece.reader.PieceReader
import com.rxjang.piece.domain.piece.store.PieceStore
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.problem.reader.ProblemReader
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PieceStatisticsServiceTest {

    private val pieceReader: PieceReader = mockk<PieceReader>()
    private val pieceStore: PieceStore = mockk<PieceStore>()
    private val authContext: AuthenticationContext = mockk<AuthenticationContext>()
    private val problemReader: ProblemReader = mockk<ProblemReader>()

    private val pieceService: PieceService = PieceService(
        problemReader = problemReader,
        pieceReader = pieceReader,
        pieceStore = pieceStore,
        authContext = authContext,
    )

    private val teacherId = TeacherId(1)
    private val pieceId = PieceId(100)
    private val studentId1 = StudentId(201)
    private val studentId2 = StudentId(202)

    @Test
    fun `존재하지 않는 학습지 조회 시 실패`() {
        // given
        every { pieceReader.findById(pieceId) } returns null

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        assertThat(result).isInstanceOf(GetPieceStaticsResult.Failure::class.java)
        val failure = result as GetPieceStaticsResult.Failure
        assertThat(failure.failureCode).isEqualTo(PieceFailureCode.PIECE_NOT_FOUND)

        verify(exactly = 0) { authContext.getCurrentTeacherId() }
    }

    @Test
    fun `다른 선생님의 학습지 조회 시 권한 없음 실패`() {
        // given
        val otherTeacherId = TeacherId(999)
        val piece = Piece(
            id = pieceId,
            title = "다른 선생님 학습지",
            teacherId = otherTeacherId,
            problemIds = listOf(ProblemId(1), ProblemId(2))
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        assertThat(result).isInstanceOf(GetPieceStaticsResult.Failure::class.java)
        val failure = result as GetPieceStaticsResult.Failure
        assertThat(failure.failureCode).isEqualTo(PieceFailureCode.UNAUTHORIZED_ACCESS)

        verify(exactly = 0) { pieceReader.findPieceAssignments(any()) }
    }

    @Test
    fun `아직 출제하지 않은 학습지 통계 조회 성공`() {
        // given
        val piece = Piece(
            id = pieceId,
            title = "미출제 학습지",
            teacherId = teacherId,
            problemIds = listOf(ProblemId(1), ProblemId(2), ProblemId(3))
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId
        every { pieceReader.findPieceAssignments(pieceId) } returns emptyList()

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        assertThat(result).isInstanceOf(GetPieceStaticsResult.Success::class.java)
        val success = result as GetPieceStaticsResult.Success

        with(success.statistics) {
            assertThat(pieceId).isEqualTo(pieceId)
            assertThat(title).isEqualTo("미출제 학습지")
            assertThat(totalAssignments).isEqualTo(0)
            assertThat(completedAssignments).isEqualTo(0)
            assertThat(averageScore).isEqualTo(0.0)
            assertThat(studentStatistics).isEmpty()
            assertThat(problemStatistics).isEmpty()
        }

        verify(exactly = 0) { pieceReader.getProblemStatisticsByPieceId(any()) }
    }

    @Test
    fun `출제된 학습지 통계 조회 성공`() {
        // given
        val piece = Piece(
            id = pieceId,
            title = "수학",
            teacherId = teacherId,
            problemIds = listOf(ProblemId(1), ProblemId(2), ProblemId(3), ProblemId(4))
        )
        val assignments = listOf(
            PieceAssignment(
                id = PieceAssignmentId(1),
                pieceId = pieceId,
                studentId = studentId1,
                status = AssignmentStatus.COMPLETED,
                score = 3,
            ),
            PieceAssignment(
                id = PieceAssignmentId(2),
                pieceId = pieceId,
                studentId = studentId2,
                status = AssignmentStatus.COMPLETED,
                score = 4,
            )
        )
        val problemStatistics = listOf(
            ProblemStatistic(
                problemId = 1,
                totalAttempts = 2,
                correctAnswers = 2,
            ),
            ProblemStatistic(
                problemId = 2,
                totalAttempts = 2,
                correctAnswers = 1,
            )
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId
        every { pieceReader.findPieceAssignments(pieceId) } returns assignments
        every { pieceReader.getProblemStatisticsByPieceId(pieceId) } returns problemStatistics

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        assertThat(result).isInstanceOf(GetPieceStaticsResult.Success::class.java)
        val success = result as GetPieceStaticsResult.Success

        with(success.statistics) {
            assertThat(pieceId).isEqualTo(pieceId)
            assertThat(title).isEqualTo("수학")
            assertThat(totalAssignments).isEqualTo(2)
            assertThat(completedAssignments).isEqualTo(2)
            assertThat(averageScore).isEqualTo(87.5)

            assertThat(studentStatistics).hasSize(2)
            assertThat(studentStatistics[0].correctRate).isEqualTo(75.0)
            assertThat(studentStatistics[1].correctRate).isEqualTo(100.0)

            assertThat(problemStatistics).hasSize(2)
        }

        verify(exactly = 1) { pieceReader.findById(pieceId) }
        verify(exactly = 1) { authContext.getCurrentTeacherId() }
        verify(exactly = 1) { pieceReader.findPieceAssignments(pieceId) }
        verify(exactly = 1) { pieceReader.getProblemStatisticsByPieceId(pieceId) }
    }

    @Test
    fun `일부만 완료된 assignment가 있는 경우`() {
        // given
        val piece = Piece(
            id = pieceId,
            title = "수학",
            teacherId = teacherId,
            problemIds = listOf(ProblemId(1), ProblemId(2))
        )

        val assignments = listOf(
            PieceAssignment(
                id = PieceAssignmentId(1),
                pieceId = pieceId,
                studentId = studentId1,
                status = AssignmentStatus.COMPLETED,
                score = 2,
            ),
            PieceAssignment(
                id = PieceAssignmentId(2),
                pieceId = pieceId,
                studentId = studentId2,
                status = AssignmentStatus.ASSIGNED,
                score = 0,
            )
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId
        every { pieceReader.findPieceAssignments(pieceId) } returns assignments
        every { pieceReader.getProblemStatisticsByPieceId(pieceId) } returns emptyList()

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        assertThat(result).isInstanceOf(GetPieceStaticsResult.Success::class.java)
        val success = result as GetPieceStaticsResult.Success

        with(success.statistics) {
            assertThat(totalAssignments).isEqualTo(2)
            assertThat(completedAssignments).isEqualTo(1)
            assertThat(averageScore).isEqualTo(100.0)

            assertThat(studentStatistics).hasSize(2)
            assertThat(studentStatistics[0].correctRate).isEqualTo(100.0)
            assertThat(studentStatistics[1].correctRate).isEqualTo(0.0)
        }
    }

    @Test
    fun `정답률 계산이 올바르게 수행되는지 확인`() {
        // given
        val piece = Piece(
            id = pieceId,
            title = "수학 테스트",
            teacherId = teacherId,
            problemIds = listOf(ProblemId(1), ProblemId(2), ProblemId(3), ProblemId(4), ProblemId(5))
        )

        val assignments = listOf(
            PieceAssignment(
                id = PieceAssignmentId(1),
                pieceId = pieceId,
                studentId = studentId1,
                status = AssignmentStatus.COMPLETED,
                score = 3,
            ),
            PieceAssignment(
                id = PieceAssignmentId(2),
                pieceId = pieceId,
                studentId = studentId2,
                status = AssignmentStatus.COMPLETED,
                score = 4,
            )
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId
        every { pieceReader.findPieceAssignments(pieceId) } returns assignments
        every { pieceReader.getProblemStatisticsByPieceId(pieceId) } returns emptyList()

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        val success = result as GetPieceStaticsResult.Success

        with(success.statistics) {
            assertThat(averageScore).isEqualTo(70.0)

            assertThat(studentStatistics[0].correctRate).isEqualTo(60.0)
            assertThat(studentStatistics[1].correctRate).isEqualTo(80.0)
        }
    }

    @Test
    fun `문제별 통계 검증`() {
        // given
        val piece = Piece(
            id = pieceId,
            title = "학습지",
            teacherId = teacherId,
            problemIds = listOf(ProblemId(1), ProblemId(2))
        )

        val assignments = listOf(
            PieceAssignment(
                id = PieceAssignmentId(1),
                pieceId = pieceId,
                studentId = studentId1,
                status = AssignmentStatus.COMPLETED,
                score = 1 // 2문제 중 1개
            )
        )

        val problemStatistics = listOf(
            ProblemStatistic(
                problemId = 1,
                totalAttempts = 1,
                correctAnswers = 1,
            ),
            ProblemStatistic(
                problemId = 2,
                totalAttempts = 1,
                correctAnswers = 0,
            )
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId
        every { pieceReader.findPieceAssignments(pieceId) } returns assignments
        every { pieceReader.getProblemStatisticsByPieceId(pieceId) } returns problemStatistics

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        assertThat(result).isInstanceOf(GetPieceStaticsResult.Success::class.java)
        val success = result as GetPieceStaticsResult.Success

        with(success.statistics) {
            assertThat(title).isEqualTo("학습지")
            assertThat(totalAssignments).isEqualTo(1)
            assertThat(completedAssignments).isEqualTo(1)
            assertThat(averageScore).isEqualTo(50.0)

            assertThat(studentStatistics).hasSize(1)
            assertThat(studentStatistics[0].studentId).isEqualTo(studentId1)
            assertThat(studentStatistics[0].score).isEqualTo(1)
            assertThat(studentStatistics[0].correctRate).isEqualTo(50.0)

            assertThat(problemStatistics).hasSize(2)
            assertThat(problemStatistics[0].correctRate).isEqualTo(100.0)
            assertThat(problemStatistics[1].correctRate).isEqualTo(0.0)
        }
    }

    @Test
    fun `0점인 학생이 있을 때 평균 계산 확인`() {
        // given
        val piece = Piece(
            id = pieceId,
            title = "학습지",
            teacherId = teacherId,
            problemIds = listOf(ProblemId(1))
        )

        val assignments = listOf(
            PieceAssignment(
                id = PieceAssignmentId(1),
                pieceId = pieceId,
                studentId = studentId1,
                status = AssignmentStatus.COMPLETED,
                score = 0,
            ),
            PieceAssignment(
                id = PieceAssignmentId(1),
                pieceId = pieceId,
                studentId = studentId2,
                status = AssignmentStatus.COMPLETED,
                score = 1
            )
        )

        every { pieceReader.findById(pieceId) } returns piece
        every { authContext.getCurrentTeacherId() } returns teacherId
        every { pieceReader.findPieceAssignments(pieceId) } returns assignments
        every { pieceReader.getProblemStatisticsByPieceId(pieceId) } returns emptyList()

        // when
        val result = pieceService.getPieceStatistics(pieceId.value)

        // then
        val success = result as GetPieceStaticsResult.Success

        with(success.statistics) {
            assertThat(averageScore).isEqualTo(50.0)
            assertThat(studentStatistics[0].correctRate).isEqualTo(0.0)
            assertThat(studentStatistics[1].correctRate).isEqualTo(100.0)
        }
    }

}