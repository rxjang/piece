package com.rxjang.piece.domain.problem.service

import com.rxjang.piece.domain.problem.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.domain.problem.query.SearchProblemQuery
import org.springframework.stereotype.Component

@Component
class ProblemLevelCountCalculator {

    fun calculateAdaptedCounts(
        query: SearchProblemQuery,
        available: ProblemCountPerLevel,
    ): ProblemCountPerLevel {
        // 원래 비율대로 계산 (실제 문제 수 반영)
        val originalCounts = query.calculateLevelCounts(available.total)

        // 가용 문제 수와 비교하여 조정
        val adaptedLow = minOf(originalCounts.lowCount, available.lowCount)
        val adaptedMedium = minOf(originalCounts.mediumCount, available.mediumCount)
        val adaptedHigh = minOf(originalCounts.highCount, available.highCount)

        val currentTotal = adaptedLow + adaptedMedium + adaptedHigh
        val shortage = query.requestCount - currentTotal
        val problemCount = ProblemCountPerLevel(adaptedLow, adaptedMedium, adaptedHigh)

        if (shortage <= 0) {
            return problemCount
        }

        // 부족한 문제를 다른 난이도로 보완
        return compensateShortage(
            currentCount = problemCount,
            shortage = shortage,
            available = available,
            requestLevel = query.level
        )
    }

    /**
     * 부족한 문제를 다른 난이도로 보완
     * @param currentCount 현재 까지 할당된 각 난이도별 문제 수 (1차 조정)
     * @param shortage 총 부족한 문제 수
     * @param available 실제 가용한 난이도별 최대 문제 수
     * @param requestLevel 요청한 주 난이도
     * @return 부족분이 보완된 최종 각 난이도별 문제 수
     */
    private fun compensateShortage(
        currentCount: ProblemCountPerLevel,
        shortage: Int,
        available: ProblemCountPerLevel,
        requestLevel: ProblemLevel
    ): ProblemCountPerLevel {

        var newLow = currentCount.lowCount
        var newMedium = currentCount.mediumCount
        var newHigh = currentCount.highCount
        var remainingShortage = shortage

        // 요청한 난이도에 따른 보완 우선순위
        val compensationOrder = getCompensationOrder(requestLevel)

        for (levelType in compensationOrder) {
            if (remainingShortage <= 0) break

            val additionalAvailableCount = when (levelType) {
                ProblemLevel.LOW -> available.lowCount - newLow
                ProblemLevel.MEDIUM -> available.mediumCount - newMedium
                ProblemLevel.HIGH -> available.highCount - newHigh
            }

            val addValue = minOf(remainingShortage, additionalAvailableCount)

            if (addValue > 0) {
                when (levelType) {
                    ProblemLevel.LOW -> newLow += addValue
                    ProblemLevel.MEDIUM -> newMedium += addValue
                    ProblemLevel.HIGH -> newHigh += addValue
                }

                remainingShortage -= addValue
            }
        }

        return ProblemCountPerLevel(newLow, newMedium, newHigh)
    }

    /**
     * 요청 난이도에 따른 보완 우선순위
     */
    private fun getCompensationOrder(requestLevel: ProblemLevel): List<ProblemLevel> {
        return when (requestLevel) {
            ProblemLevel.HIGH -> listOf(ProblemLevel.HIGH, ProblemLevel.MEDIUM, ProblemLevel.LOW)
            ProblemLevel.MEDIUM -> listOf(ProblemLevel.MEDIUM, ProblemLevel.LOW, ProblemLevel.HIGH)
            ProblemLevel.LOW -> listOf(ProblemLevel.LOW, ProblemLevel.MEDIUM, ProblemLevel.HIGH)
        }
    }
}