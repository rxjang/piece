package com.rxjang.piece.infrastructure.persistance.mapper

import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity

object PieceMapper {

    fun PieceEntity.toModel(withProblems: Boolean): Piece {
        return Piece(
            id = PieceId(this.id!!),
            title = this.title,
            userId = TeacherId(this.userId),
            problemIds = if (withProblems) {
                this.problems.map { ProblemId(it.problemId) }
            } else emptyList()
        )
    }
}