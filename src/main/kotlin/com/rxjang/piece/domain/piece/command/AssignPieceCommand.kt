package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.user.model.StudentId

data class AssignPieceCommand(
    val pieceId: PieceId,
    val studentIds: List<StudentId>,
)
