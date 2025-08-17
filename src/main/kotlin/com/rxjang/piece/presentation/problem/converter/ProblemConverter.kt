package com.rxjang.piece.presentation.problem.converter

import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.presentation.problem.dto.response.ProblemResponse

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