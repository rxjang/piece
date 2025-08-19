package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.PieceProblemEntity
import com.rxjang.piece.infrastructure.persistance.entity.PieceProblemId
import org.springframework.data.jpa.repository.JpaRepository

interface PieceProblemRepository: JpaRepository<PieceProblemEntity, PieceProblemId> {

    fun findByPieceIdAndProblemIdIn(pieceId: Int, problemIds: List<Int>): List<PieceProblemEntity>
}