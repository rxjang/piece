package com.rxjang.piece.domain.problem.dto

/**
 * 난이도 별 문제 수
 */
data class ProblemCountPerLevel(
    val lowCount: Int,
    val mediumCount: Int,
    val highCount: Int
) {

    val total = lowCount + mediumCount + highCount
}