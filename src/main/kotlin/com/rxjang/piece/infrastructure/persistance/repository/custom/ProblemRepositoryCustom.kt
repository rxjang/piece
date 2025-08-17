package com.rxjang.piece.infrastructure.persistance.repository.custom

import com.rxjang.piece.application.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity
import org.springframework.data.domain.Pageable

interface ProblemRepositoryCustom {

    fun getProblemCountsByLevel(unitCodes: List<String>, type: ProblemType): ProblemCountPerLevel

    fun searchProblems(
        unitCodes: List<String>,
        type: ProblemType,
        levels: List<Int>,
        requestCount: Int,
    ): List<ProblemEntity>
}