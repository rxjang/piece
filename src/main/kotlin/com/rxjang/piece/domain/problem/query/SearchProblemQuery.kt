package com.rxjang.piece.domain.problem.query

import com.rxjang.piece.domain.problem.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.domain.problem.model.ProblemType

/**
 * 문제 검색 query
 * @param requestCount 총 요청 문제수
 * @param unitCodes 유형 코드 목록
 * @param problemTypes 문제 유형
 * @param level 난이도
 */
data class SearchProblemQuery(
    val requestCount: Int,
    val unitCodes: List<String>,
    val problemTypes: List<ProblemType>,
    val level: ProblemLevel,
) {

    /**
     * 요청 난이도에 따른 문제 수 계산
     * @param availableCount 실제 가용 문제 수
     */
    fun calculateLevelCounts(availableCount: Int?): ProblemCountPerLevel {
        val ratio = level.getLevelRatio()
        val totalCount = if (availableCount == null || availableCount > requestCount) requestCount else availableCount

        var lowCount = (totalCount * ratio.low).toInt()
        var mediumCount = (totalCount * ratio.medium).toInt()
        var highCount = (totalCount * ratio.high).toInt()

        // 반올림으로 인한 차이를 보정 (가장 비율이 높은 곳에 추가/차감)
        val calculatedTotal = lowCount + mediumCount + highCount
        val diff = totalCount - calculatedTotal

        when (level) {
            ProblemLevel.HIGH -> highCount += diff
            ProblemLevel.MEDIUM -> mediumCount += diff
            ProblemLevel.LOW -> lowCount += diff
        }

        return ProblemCountPerLevel(
            lowCount = maxOf(0, lowCount),
            mediumCount = maxOf(0, mediumCount),
            highCount = maxOf(0, highCount)
        )
    }
}