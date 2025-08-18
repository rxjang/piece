package com.rxjang.piece.domain.piece.reader

import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.user.model.StudentId

interface PieceReader {

    fun findById(id: PieceId): Piece?

    fun findProblemsInPieceForStudent(pieceId: PieceId, studentId: StudentId): List<Problem>

    fun findAlreadyAssignedStudents(command: AssignPieceCommand): List<StudentId>
}