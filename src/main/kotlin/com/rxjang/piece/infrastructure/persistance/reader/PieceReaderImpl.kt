package com.rxjang.piece.infrastructure.persistance.reader

import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.reader.PieceReader
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.infrastructure.persistance.mapper.PieceMapper.toModel
import com.rxjang.piece.infrastructure.persistance.repository.PieceAssignmentRepository
import com.rxjang.piece.infrastructure.persistance.repository.PieceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PieceReaderImpl(
    private val pieceRepository: PieceRepository,
    private val pieceAssignmentRepository: PieceAssignmentRepository,
): PieceReader {

    override fun findById(id: PieceId): Piece? {
        return pieceRepository.findByIdWithProblems(id.value)?.toModel(true)
    }

    override fun findAlreadyAssignedStudents(command: AssignPieceCommand): List<StudentId> {
        return pieceAssignmentRepository.findByPieceIdAndStudentIdIn(command.pieceId.value, command.studentIds.map { it.value })
            .map { StudentId(it.studentId) }
    }


}