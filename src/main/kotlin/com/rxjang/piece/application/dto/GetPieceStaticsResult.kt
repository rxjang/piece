package com.rxjang.piece.application.dto

import com.rxjang.piece.application.common.PieceFailureCode
import com.rxjang.piece.domain.piece.model.PieceStatistics

sealed class GetPieceStaticsResult{
    data class Success(val statistics: PieceStatistics): GetPieceStaticsResult()
    data class Failure(val failureCode: PieceFailureCode): GetPieceStaticsResult()
}
