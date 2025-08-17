package com.rxjang.piece.application.dto

/**
 * 난이도 별 문제 수
 */
data class ProblemCountPerLevel(
    val lowCount: Int,
    val mediumCount: Int,
    val highCount: Int
) {

    // TODO queryDsl 적용 후 제거, jpql 반환용
    constructor(lowCount: Long, mediumCount: Long, highCount: Long) : this(lowCount.toInt(), mediumCount.toInt(), highCount.toInt())

    val total = lowCount + mediumCount + highCount
}
