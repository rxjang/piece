package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.piece.model.PieceAssignmentId

data class UpdatePieceAssignmentCommand(
    val pieceAssignmentId: PieceAssignmentId,
    val score: Int,
)