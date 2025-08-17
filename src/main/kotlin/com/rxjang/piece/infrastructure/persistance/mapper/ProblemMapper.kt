package com.rxjang.piece.infrastructure.persistance.mapper

import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity

object ProblemMapper {

    fun ProblemEntity.toModel(): Problem {

        return Problem(
            id = ProblemId(this.id!!),
            unitCode = this.unitCode,
            level = level,
            type = type,
            answer = answer,
        )
    }
}