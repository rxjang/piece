package com.rxjang.piece.application.dto

import com.rxjang.piece.application.common.FailureCode
import com.rxjang.piece.domain.piece.model.PieceAssignment

sealed class AssignPieceResult {
    data class Success(val assignments: List<PieceAssignment>): AssignPieceResult()
    data class Failure(val failureCode: FailureCode): AssignPieceResult()
}
