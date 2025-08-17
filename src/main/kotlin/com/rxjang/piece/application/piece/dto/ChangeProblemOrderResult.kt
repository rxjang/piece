package com.rxjang.piece.application.piece.dto

import com.rxjang.piece.application.piece.PieceFailureCode
import com.rxjang.piece.domain.piece.model.Piece

sealed class ChangeProblemOrderResult

data class ChangeProblemOrderSuccess(
    val piece: Piece,
): ChangeProblemOrderResult()

data class ChangeProblemOrderFailure(
    val failureCode: PieceFailureCode,
): ChangeProblemOrderResult()
