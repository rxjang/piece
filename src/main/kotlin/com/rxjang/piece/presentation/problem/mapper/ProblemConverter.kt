package com.rxjang.piece.presentation.problem.mapper

import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.presentation.problem.response.ProblemResponse

object ProblemConverter {

    fun Problem.toResponse(): ProblemResponse {
        return ProblemResponse(
            id = this.id.value,
            answer = this.answer,
            level = this.level,
            unitCode = this.unitCode,
            problemType = this.type,
        )
    }
}