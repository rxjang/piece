package com.rxjang.piece.presentation.piece

import com.rxjang.piece.application.common.PieceFailureCode
import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.service.PieceService
import com.rxjang.piece.application.dto.ChangeProblemOrderFailure
import com.rxjang.piece.application.dto.ChangeProblemOrderSuccess
import com.rxjang.piece.application.dto.CreatePieceFailure
import com.rxjang.piece.application.dto.CreatePieceSuccess
import com.rxjang.piece.application.dto.GetPieceStaticsResult
import com.rxjang.piece.application.dto.ScorePieceResult
import com.rxjang.piece.application.facade.PieceFacade
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.model.PieceStatistics
import com.rxjang.piece.domain.piece.query.GetPieceStatisticsQuery
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.infrastructure.security.config.SecurityConstants.STUDENT_ROLE
import com.rxjang.piece.infrastructure.security.config.SecurityConstants.TEACHER_ROLE
import com.rxjang.piece.presentation.exception.BusinessException
import com.rxjang.piece.presentation.piece.converter.PieceConverter.toChangeOrderResponse
import com.rxjang.piece.presentation.piece.converter.PieceConverter.toCommand
import com.rxjang.piece.presentation.piece.dto.request.AssignPieceToStudentRequest
import com.rxjang.piece.presentation.piece.dto.request.ChangeProblemOrderInPieceRequest
import com.rxjang.piece.presentation.piece.dto.request.CreatePieceRequest
import com.rxjang.piece.presentation.piece.dto.request.ScorePieceRequest
import com.rxjang.piece.presentation.piece.dto.response.AssignPieceToStudentResponse
import com.rxjang.piece.presentation.piece.dto.response.ChangeProblemOrderInPieceResponse
import com.rxjang.piece.presentation.piece.dto.response.CreatePieceResponse
import com.rxjang.piece.presentation.piece.dto.response.ScorePieceResponse
import com.rxjang.piece.presentation.problem.converter.ProblemConverter.toNoAnswerResponse
import com.rxjang.piece.presentation.problem.dto.response.ProblemWithNoAnswerResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/piece")
@Validated
class PieceController(
    private val pieceFacade: PieceFacade,
    private val pieceService: PieceService,
) {

    @PostMapping
    @PreAuthorize(TEACHER_ROLE)
    fun createPiece(@RequestBody @Valid request: CreatePieceRequest): ResponseEntity<CreatePieceResponse> {
        val result = pieceService.createPiece(request.toCommand())
        return when(result) {
            is CreatePieceSuccess ->
                ResponseEntity
                    .ok()
                    .body(
                        CreatePieceResponse(result.piece.id.value)
                    )
            is CreatePieceFailure -> throw BusinessException(result.failureCode)
        }
    }

    @PatchMapping("/{pieceId}/order")
    @PreAuthorize(TEACHER_ROLE)
    fun changeProblemOrder(
        @PathVariable pieceId: Int,
        @RequestBody @Valid request: ChangeProblemOrderInPieceRequest
    ): ResponseEntity<ChangeProblemOrderInPieceResponse> {
        val result = pieceService.changeProblemOrder(request.toCommand(pieceId))
        return when (result) {
            is ChangeProblemOrderSuccess ->
                ResponseEntity.ok()
                    .body(result.piece.toChangeOrderResponse())
            is ChangeProblemOrderFailure -> throw BusinessException(result.failureCode)
        }
    }

    @PostMapping("/{pieceId}")
    @PreAuthorize(TEACHER_ROLE)
    fun assignToStudent(
        @PathVariable pieceId: Int,
        @RequestBody @Valid request: AssignPieceToStudentRequest
    ): ResponseEntity<AssignPieceToStudentResponse> {
        val command = request.toCommand(pieceId)
        val result = pieceFacade.assignPieceToStudent(command)
        return when (result) {
            is AssignPieceResult.Success ->
                ResponseEntity
                    .ok()
                    .body(AssignPieceToStudentResponse(result.assignments.map { it.studentId.value }))
            is AssignPieceResult.Failure -> throw BusinessException(result.failureCode)
        }
    }

    @GetMapping("/{pieceId}/problems")
    @PreAuthorize(STUDENT_ROLE)
    fun getPiece(
        @PathVariable pieceId: Int
    ): ResponseEntity<List<ProblemWithNoAnswerResponse>> {
        val problems = pieceService.findProblemsInPieceForStudent(PieceId(pieceId))
        return if (problems.isEmpty()) {
            throw BusinessException(PieceFailureCode.PIECE_NOT_FOUND)
        } else {
            ResponseEntity.ok().body(problems.map { it.toNoAnswerResponse() })
        }
    }

    @PutMapping("/{pieceId}/score")
    @PreAuthorize(STUDENT_ROLE)
    fun scorePiece(
        @PathVariable pieceId: Int,
        @RequestBody @Valid request: ScorePieceRequest): ResponseEntity<ScorePieceResponse> {
        val command = request.toCommand(pieceId)
        val result = pieceService.score(command)
        return when (result) {
            is ScorePieceResult.Success ->
                ResponseEntity
                    .ok()
                    .body(ScorePieceResponse(result.score))
            is ScorePieceResult.Failure -> throw BusinessException(result.failureCode)
        }
    }

    /**
     * 학습지 통계
     */
    @GetMapping("/{pieceId}/analyze")
    @PreAuthorize(TEACHER_ROLE)
    fun analyzePiece(
        @PathVariable pieceId: Int,
    ): ResponseEntity<PieceStatistics> {
        val query = GetPieceStatisticsQuery(pieceId = PieceId(pieceId))
        val result = pieceService.getPieceStatistics(query)
        return when (result) {
            is GetPieceStaticsResult.Success ->
                ResponseEntity
                    .ok()
                    .body(result.statistics)
            is GetPieceStaticsResult.Failure -> throw BusinessException(result.failureCode)
        }
    }
}