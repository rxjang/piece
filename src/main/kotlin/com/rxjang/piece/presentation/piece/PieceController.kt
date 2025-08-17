package com.rxjang.piece.presentation.piece

import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.service.PieceService
import com.rxjang.piece.application.dto.ChangeProblemOrderFailure
import com.rxjang.piece.application.dto.ChangeProblemOrderSuccess
import com.rxjang.piece.application.dto.CreatePieceFailure
import com.rxjang.piece.application.dto.CreatePieceSuccess
import com.rxjang.piece.application.facade.PieceFacade
import com.rxjang.piece.presentation.exception.BusinessException
import com.rxjang.piece.presentation.piece.converter.PieceConverter.toChangeOrderResponse
import com.rxjang.piece.presentation.piece.converter.PieceConverter.toCommand
import com.rxjang.piece.presentation.piece.dto.request.AssignPieceToStudentRequest
import com.rxjang.piece.presentation.piece.dto.request.ChangeProblemOrderInPieceRequest
import com.rxjang.piece.presentation.piece.dto.request.CreatePieceRequest
import com.rxjang.piece.presentation.piece.dto.response.AssignPieceToStudentResponse
import com.rxjang.piece.presentation.piece.dto.response.ChangeProblemOrderInPieceResponse
import com.rxjang.piece.presentation.piece.dto.response.CreatePieceResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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
}