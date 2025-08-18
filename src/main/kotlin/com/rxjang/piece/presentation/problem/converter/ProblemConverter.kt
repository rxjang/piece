package com.rxjang.piece.presentation.problem.converter

import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.presentation.problem.dto.response.ProblemResponse
import com.rxjang.piece.presentation.problem.dto.response.ProblemWithNoAnswerResponse

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

    fun Problem.toNoAnswerResponse(): ProblemWithNoAnswerResponse {
        return ProblemWithNoAnswerResponse(
            id = this.id.value,
            level = this.level,
            unit = this.unitCode,
            problemType = this.type,
        )
    }
}