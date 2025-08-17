package com.rxjang.piece.application.dto

import com.rxjang.piece.application.common.PieceFailureCode
import com.rxjang.piece.domain.piece.model.Piece

sealed class CreatePieceResult

data class CreatePieceSuccess(
    val piece: Piece,
): CreatePieceResult()

data class CreatePieceFailure(
    val failureCode: PieceFailureCode,
): CreatePieceResult() {

}