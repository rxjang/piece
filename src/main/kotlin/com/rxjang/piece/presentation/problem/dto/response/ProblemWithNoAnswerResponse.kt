package com.rxjang.piece.presentation.problem.dto.response

import com.rxjang.piece.domain.problem.model.ProblemType

data class ProblemWithNoAnswerResponse(
    val id: Int,
    val level: Int,
    val unit: String,
    val problemType: ProblemType,
)