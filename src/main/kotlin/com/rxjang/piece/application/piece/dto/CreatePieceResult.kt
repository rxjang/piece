package com.rxjang.piece.application.piece.dto

import com.rxjang.piece.application.common.dto.CommandFailure
import com.rxjang.piece.application.common.dto.CommandSuccess
import com.rxjang.piece.application.piece.PieceFailureCode
import com.rxjang.piece.domain.piece.model.Piece

sealed class CreatePieceResult

data class CreatePieceSuccess(
    val piece: Piece,
): CreatePieceResult(), CommandSuccess

data class CreatePieceFailure(
    override val failureCode: PieceFailureCode,
): CreatePieceResult(), CommandFailure {

}