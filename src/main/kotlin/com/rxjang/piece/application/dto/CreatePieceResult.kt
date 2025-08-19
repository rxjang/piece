package com.rxjang.piece.application.dto

import com.rxjang.piece.application.exception.codes.PieceFailureCode
import com.rxjang.piece.domain.piece.model.Piece

sealed class CreatePieceResult {
    data class Success(val piece: Piece) : CreatePieceResult()
    data class Failure(val failureCode: PieceFailureCode) : CreatePieceResult()
}