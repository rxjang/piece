package com.rxjang.piece.domain.piece.reader

import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.user.model.StudentId

interface PieceReader {

    fun findById(id: PieceId): Piece?

    fun findAlreadyAssignedStudents(command: AssignPieceCommand): List<StudentId>
}