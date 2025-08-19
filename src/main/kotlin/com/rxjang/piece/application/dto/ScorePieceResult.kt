package com.rxjang.piece.application.dto

import com.rxjang.piece.application.exception.codes.PieceFailureCode

sealed class ScorePieceResult{
    data class Success(val score: Int) : ScorePieceResult()
    data class Failure(val failureCode: PieceFailureCode) : ScorePieceResult()
}
