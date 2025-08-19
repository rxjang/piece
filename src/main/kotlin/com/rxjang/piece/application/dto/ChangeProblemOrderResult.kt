package com.rxjang.piece.application.dto

import com.rxjang.piece.application.exception.codes.PieceFailureCode
import com.rxjang.piece.domain.piece.model.Piece

sealed class ChangeProblemOrderResult {
    data class Success(val piece: Piece) : ChangeProblemOrderResult()
    data class Failure(val failureCode: PieceFailureCode) : ChangeProblemOrderResult()
}