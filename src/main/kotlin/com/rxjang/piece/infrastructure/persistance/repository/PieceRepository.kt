package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity
import com.rxjang.piece.infrastructure.persistance.repository.custom.PieceRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PieceRepository: JpaRepository<PieceEntity, Int>, PieceRepositoryCustom {

    @Query("""
        SELECT p 
        FROM PieceEntity p 
        JOIN FETCH PieceProblemEntity pp on p.id = pp.pieceId
        WHERE p.id = :id
    """)
    fun findByIdWithProblems(id: Int): PieceEntity?
}