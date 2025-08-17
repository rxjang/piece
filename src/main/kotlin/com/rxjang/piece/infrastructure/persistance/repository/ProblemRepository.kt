package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.application.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProblemRepository: JpaRepository<ProblemEntity, Int> {

    @Query("""
        SELECT new com.rxjang.piece.application.dto.ProblemCountPerLevel(
            COUNT(CASE WHEN p.level = 1 THEN 1 END),
            COUNT(CASE WHEN p.level IN (2, 3, 4) THEN 1 END),
            COUNT(CASE WHEN p.level = 5 THEN 1 END)
        )
        FROM ProblemEntity p
        JOIN UnitCodeEntity u on p.unitCode = u.unitCode 
        WHERE p.unitCode IN :unitCodes 
        AND p.type = :type
    """)
    fun getProblemCountsByLevel(
        unitCodes: List<String>,
        type: ProblemType,
    ): ProblemCountPerLevel

    @Query("""
        SELECT p
        FROM ProblemEntity p 
        JOIN UnitCodeEntity u on p.unitCode = u.unitCode 
        WHERE p.unitCode IN :unitCodes 
        AND p.type = :type
        AND p.level in :levels
        ORDER BY p.unitCode, p.type, p.level
    """)
    fun searchProblems(
        unitCodes: List<String>,
        type: ProblemType,
        levels: List<Int>,
        pageable: Pageable,
    ): List<ProblemEntity>
}