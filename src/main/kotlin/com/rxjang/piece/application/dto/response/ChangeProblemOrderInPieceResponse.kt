package com.rxjang.piece.application.dto.response

import com.rxjang.piece.application.dto.request.ProblemOrder

data class ChangeProblemOrderInPieceResponse(
    val pieceId: Int,
    val problemOrders: List<ProblemOrder>
)
