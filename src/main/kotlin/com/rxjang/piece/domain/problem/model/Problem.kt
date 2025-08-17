package com.rxjang.piece.domain.problem.model

data class Problem(
    val id: ProblemId,
    val unitCode: String, // 유형 코드
    val level: Int, // 난이도
    val type: ProblemType, // 문제 유형
    val answer: String,
)

@JvmInline
value class ProblemId(
    val value: Int,
)