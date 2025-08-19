package com.rxjang.piece.infrastructure.persistance.mapper

import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceAssignment
import com.rxjang.piece.domain.piece.model.PieceAssignmentId
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.infrastructure.persistance.entity.PieceAssignmentEntity
import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity

object PieceMapper {

    fun PieceEntity.toModel(withProblems: Boolean): Piece {
        return Piece(
            id = PieceId(this.id!!),
            title = this.title,
            teacherId = TeacherId(this.userId),
            problemIds = if (withProblems) {
                this.problems.map { ProblemId(it.id.problemId) }
            } else emptyList()
        )
    }

    fun PieceAssignmentEntity.toModel(): PieceAssignment {
        return PieceAssignment(
            id = PieceAssignmentId(this.id!!),
            pieceId = PieceId(this.pieceId),
            studentId = StudentId(this.studentId),
            status = this.status,
            score = this.score,
        )
    }
}