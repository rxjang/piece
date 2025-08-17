package com.rxjang.piece.infrastructure.persistance.repository.custom

import com.rxjang.piece.domain.problem.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity

interface ProblemRepositoryCustom {

    fun getProblemCountsByLevel(unitCodes: List<String>, types: List<ProblemType>): ProblemCountPerLevel

    fun searchProblems(
        unitCodes: List<String>,
        types: List<ProblemType>,
        levels: List<Int>,
        requestCount: Int,
    ): List<ProblemEntity>
}