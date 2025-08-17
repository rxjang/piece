package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PieceRepository: JpaRepository<PieceEntity, Int> {

    @Query("""
        SELECT p 
        FROM PieceEntity p 
        JOIN FETCH PieceProblemEntity pp on p.id = pp.pieceId
        WHERE p.id = :id
    """)
    fun findByIdWithProblems(id: Int): PieceEntity?
}