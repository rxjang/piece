package com.rxjang.piece.presentation.piece.dto.response

import com.rxjang.piece.presentation.piece.dto.request.ProblemOrder

data class ChangeProblemOrderInPieceResponse(
    val pieceId: Int,
    val problemOrders: List<ProblemOrder>
)
