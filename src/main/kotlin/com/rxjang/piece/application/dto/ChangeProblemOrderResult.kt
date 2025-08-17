package com.rxjang.piece.application.dto

import com.rxjang.piece.application.common.PieceFailureCode
import com.rxjang.piece.domain.piece.model.Piece

sealed class ChangeProblemOrderResult

data class ChangeProblemOrderSuccess(
    val piece: Piece,
): ChangeProblemOrderResult()

data class ChangeProblemOrderFailure(
    val failureCode: PieceFailureCode,
): ChangeProblemOrderResult()
