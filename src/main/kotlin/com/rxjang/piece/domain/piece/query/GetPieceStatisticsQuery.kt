package com.rxjang.piece.domain.piece.query

import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.user.model.TeacherId

data class GetPieceStatisticsQuery(
    val pieceId: PieceId,
)
