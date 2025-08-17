package com.rxjang.piece.presentation.problem.response

import com.rxjang.piece.domain.problem.model.ProblemType

data class SearchProblemResponse(
    val problemList: List<ProblemResponse>,
)

data class ProblemResponse(
    val id: Int,
    val answer: String,
    val level: Int,
    val unitCode: String,
    val problemType: ProblemType,
)
