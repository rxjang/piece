package com.rxjang.piece.presentation.piece

import com.rxjang.piece.application.exception.codes.PieceFailureCode
import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.dto.ChangeProblemOrderResult
import com.rxjang.piece.application.dto.CreatePieceResult
import com.rxjang.piece.application.service.PieceService
import com.rxjang.piece.application.dto.GetPieceStaticsResult
import com.rxjang.piece.application.dto.ScorePieceResult
import com.rxjang.piece.application.facade.PieceFacade
import com.rxjang.piece.domain.piece.model.PieceStatistics
import com.rxjang.piece.presentation.security.SecurityConstants.STUDENT_ROLE
import com.rxjang.piece.presentation.security.SecurityConstants.TEACHER_ROLE
import com.rxjang.piece.application.exception.BusinessException
import com.rxjang.piece.application.dto.converter.PieceConverter.toChangeOrderResponse
import com.rxjang.piece.application.dto.request.AssignPieceToStudentRequest
import com.rxjang.piece.application.dto.request.ChangeProblemOrderInPieceRequest
import com.rxjang.piece.application.dto.request.CreatePieceRequest
import com.rxjang.piece.application.dto.request.ScorePieceRequest
import com.rxjang.piece.application.dto.response.AssignPieceToStudentResponse
import com.rxjang.piece.application.dto.response.ChangeProblemOrderInPieceResponse
import com.rxjang.piece.application.dto.response.CreatePieceResponse
import com.rxjang.piece.application.dto.response.ScorePieceResponse
import com.rxjang.piece.application.dto.converter.ProblemConverter.toNoAnswerResponse
import com.rxjang.piece.application.dto.response.ProblemWithNoAnswerResponse
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
        val result = pieceService.createPiece(request)
        return when(result) {
            is CreatePieceResult.Success ->
                ResponseEntity
                    .ok()
                    .body(
                        CreatePieceResponse(result.piece.id.value)
                    )
            is CreatePieceResult.Failure -> throw BusinessException(result.failureCode)
        }
    }

    @PatchMapping("/{pieceId}/order")
    @PreAuthorize(TEACHER_ROLE)
    fun changeProblemOrder(
        @PathVariable pieceId: Int,
        @RequestBody @Valid request: ChangeProblemOrderInPieceRequest
    ): ResponseEntity<ChangeProblemOrderInPieceResponse> {
        val result = pieceService.changeProblemOrder(pieceId, request)
        return when (result) {
            is ChangeProblemOrderResult.Success ->
                ResponseEntity.ok()
                    .body(result.piece.toChangeOrderResponse())
            is ChangeProblemOrderResult.Failure -> throw BusinessException(result.failureCode)
        }
    }

    @PostMapping("/{pieceId}")
    @PreAuthorize(TEACHER_ROLE)
    fun assignToStudent(
        @PathVariable pieceId: Int,
        @RequestBody @Valid request: AssignPieceToStudentRequest
    ): ResponseEntity<AssignPieceToStudentResponse> {
        val result = pieceFacade.assignPieceToStudent(pieceId, request)
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
        val problems = pieceService.findProblemsInPieceForStudent(pieceId)
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
        val result = pieceService.score(pieceId, request)
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
        val result = pieceService.getPieceStatistics(pieceId)
        return when (result) {
            is GetPieceStaticsResult.Success ->
                ResponseEntity
                    .ok()
                    .body(result.statistics)
            is GetPieceStaticsResult.Failure -> throw BusinessException(result.failureCode)
        }
    }
}
