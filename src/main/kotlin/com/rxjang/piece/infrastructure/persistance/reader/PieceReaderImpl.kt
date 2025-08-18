package com.rxjang.piece.infrastructure.persistance.reader

import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceAssignment
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.reader.PieceReader
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.infrastructure.persistance.mapper.PieceMapper.toModel
import com.rxjang.piece.infrastructure.persistance.mapper.ProblemMapper.toModel
import com.rxjang.piece.infrastructure.persistance.repository.PieceAssignmentRepository
import com.rxjang.piece.infrastructure.persistance.repository.PieceRepository
import org.springframework.stereotype.Repository

@Repository
class PieceReaderImpl(
    private val pieceRepository: PieceRepository,
    private val pieceAssignmentRepository: PieceAssignmentRepository,
): PieceReader {

    override fun findById(id: PieceId): Piece? {
        return pieceRepository.findByIdWithProblems(id.value)?.toModel(true)
    }

    override fun findPieceAssignment(
        pieceId: PieceId,
        studentId: StudentId
    ): PieceAssignment? {
        return pieceAssignmentRepository.findByPieceIdAndStudentId(pieceId.value, studentId.value)?.toModel()
    }

    override fun findProblemsInPieceForStudent(
        pieceId: PieceId,
        studentId: StudentId
    ): List<Problem> {
        val problmes = pieceRepository.findProblemsInPieceForStudent(pieceId.value, studentId.value)
        return problmes.map { it.toModel() }
    }

    override fun findAlreadyAssignedStudents(command: AssignPieceCommand): List<StudentId> {
        return pieceAssignmentRepository.findByPieceIdAndStudentIdIn(command.pieceId.value, command.studentIds.map { it.value })
            .map { StudentId(it.studentId) }
    }


}