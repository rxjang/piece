package com.rxjang.piece.domain.piece.model

import com.rxjang.piece.domain.user.model.StudentId

data class PieceAssignment(
    val id: PieceAssignmentId,
    val pieceId: PieceId,
    val studentId: StudentId,
    val status: AssignmentStatus,
)

@JvmInline
value class PieceAssignmentId(val id: Int)
