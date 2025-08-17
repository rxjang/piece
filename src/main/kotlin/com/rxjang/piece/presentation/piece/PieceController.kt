package com.rxjang.piece.presentation.piece

import com.rxjang.piece.application.piece.PieceService
import com.rxjang.piece.application.piece.dto.CreatePieceFailure
import com.rxjang.piece.application.piece.dto.CreatePieceSuccess
import com.rxjang.piece.presentation.exception.BusinessException
import com.rxjang.piece.presentation.piece.converter.PieceConverter.toCommand
import com.rxjang.piece.presentation.piece.dto.request.CreatePieceRequest
import com.rxjang.piece.presentation.piece.dto.response.CreatePieceResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/piece")
@Validated
class PieceController(
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
}