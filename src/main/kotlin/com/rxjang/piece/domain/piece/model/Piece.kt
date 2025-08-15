package com.rxjang.piece.domain.piece.model

import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.UserId

data class Piece(
    val id: PieceId,
    val title: String,
    val userId: UserId,
    val problemIds: List<ProblemId>
)

@JvmInline
value class PieceId(val value: Int)