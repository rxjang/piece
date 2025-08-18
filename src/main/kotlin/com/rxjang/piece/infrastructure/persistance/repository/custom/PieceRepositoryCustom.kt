package com.rxjang.piece.infrastructure.persistance.repository.custom

import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity

interface PieceRepositoryCustom {

    fun findProblemsInPieceForStudent(pieceId: Int, studentId: Int): List<ProblemEntity>
}