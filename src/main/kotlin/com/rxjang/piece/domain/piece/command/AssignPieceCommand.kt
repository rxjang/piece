package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId

data class AssignPieceCommand(
    val teacherId: TeacherId,
    val pieceId: PieceId,
    val studentIds: List<StudentId>,
)
