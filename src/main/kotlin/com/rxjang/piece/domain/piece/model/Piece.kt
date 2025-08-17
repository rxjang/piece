package com.rxjang.piece.domain.piece.model

import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.TeacherId

data class Piece(
    val id: PieceId,
    val title: String,
    val teacherId: TeacherId,
    val problemIds: List<ProblemId>
)

@JvmInline
value class PieceId(val value: Int)