package com.rxjang.piece.application.dto.response

import com.rxjang.piece.domain.problem.model.ProblemType

data class ProblemWithNoAnswerResponse(
    val id: Int,
    val level: Int,
    val unit: String,
    val problemType: ProblemType,
)