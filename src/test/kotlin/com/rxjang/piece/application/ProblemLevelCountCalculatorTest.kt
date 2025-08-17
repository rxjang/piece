package com.rxjang.piece.application

import com.rxjang.piece.domain.problem.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.service.ProblemLevelCountCalculator
import com.rxjang.piece.domain.problem.query.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ProblemLevelCountCalculatorTest {

    private val problemLevelCountCalculator = ProblemLevelCountCalculator()

    // ** 문제가 충분한 경우 */
    @Test
    fun `충분한 문제가 있는 경우 원래 비율대로 반환 (난이도 상)`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 10,
            unitCodes = listOf("uc1523"),
            problemTypes = listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH // 상: 하20%, 중30%, 상50%
        )

        val available = ProblemCountPerLevel(
            lowCount = 50,
            mediumCount = 50,
            highCount = 50,
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.lowCount).isEqualTo(2)    // 10 * 20% = 2
        assertThat(result.mediumCount).isEqualTo(3) // 10 * 30% = 3
        assertThat(result.highCount).isEqualTo(5)   // 10 * 50% = 5
        assertThat(result.total).isEqualTo(10)
    }

    @Test
    fun `충분한 문제가 있는 경우 원래 비율대로 반환 (난이도 중)`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 20,
            unitCodes = listOf("uc1521"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.MEDIUM
        )

        val available = ProblemCountPerLevel(
            lowCount = 100,
            mediumCount = 100,
            highCount = 100
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.lowCount).isEqualTo(5)    // 20 * 25% = 5
        assertThat(result.mediumCount).isEqualTo(10) // 20 * 50% = 10
        assertThat(result.highCount).isEqualTo(5)   // 20 * 25% = 5
        assertThat(result.total).isEqualTo(20)
    }

    @Test
    fun `충분한 문제가 있는 경우 원래 비율대로 반환 (난이도 히)`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 30,
            unitCodes = listOf("uc1526"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.LOW  // 하: 하50%, 중30%, 상20%
        )

        val available = ProblemCountPerLevel(
            lowCount = 100,
            mediumCount = 100,
            highCount = 100
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.lowCount).isEqualTo(15)   // 30 * 50% = 15
        assertThat(result.mediumCount).isEqualTo(9) // 30 * 30% = 9
        assertThat(result.highCount).isEqualTo(6)   // 30 * 20% = 6
        assertThat(result.total).isEqualTo(30)
    }

    // ** 문제가 충분하지 않은 경우 */

    @Test
    fun `문제가 충분하지 않은 경우 (난이도 상)`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 10,
            unitCodes = listOf("uc1537"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH
        )

        val available = ProblemCountPerLevel(
            lowCount = 10,
            mediumCount = 10,
            highCount = 2
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.lowCount).isEqualTo(2)
        assertThat(result.mediumCount).isEqualTo(6)
        assertThat(result.highCount).isEqualTo(2)
        assertThat(result.total).isEqualTo(10)
    }

    @Test
    fun `문제가 충분하지 않은 경우 (난이도 중)`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 20,
            unitCodes = listOf("uc1539"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.MEDIUM
        )

        val available = ProblemCountPerLevel(
            lowCount = 10,
            mediumCount = 3,
            highCount = 10
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.lowCount).isGreaterThan(5)
        assertThat(result.mediumCount).isEqualTo(3)
        assertThat(result.highCount).isGreaterThan(5)
        assertThat(result.total).isEqualTo(20)
    }

    @Test
    fun `문제가 충분하지 않은 경우 (난이도 하)`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 10,
            unitCodes = listOf("uc1540"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.LOW
        )

        val available = ProblemCountPerLevel(
            lowCount = 1,
            mediumCount = 10,
            highCount = 10
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.lowCount).isEqualTo(1)
        assertThat(result.mediumCount).isEqualTo(7)
        assertThat(result.highCount).isEqualTo(2)
        assertThat(result.total).isEqualTo(10)
    }

    //** 엣지 케이스 */

    @Test
    fun `전체 가용 문제가 요청수보다 적은 경우 가용한 문제만 반환 한다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 100,
            unitCodes = listOf("uc1548"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH
        )

        val available = ProblemCountPerLevel(
            lowCount = 5,
            mediumCount = 10,
            highCount = 3
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(18)
        assertThat(result.lowCount).isEqualTo(5)
        assertThat(result.mediumCount).isEqualTo(10)
        assertThat(result.highCount).isEqualTo(3)
    }

    @Test
    fun `모든 난이도가 부족한 경우 가용한 모든 문제가 반환된다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 100,
            unitCodes = listOf("uc1570"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH
        )

        val available = ProblemCountPerLevel(
            lowCount = 5,
            mediumCount = 8,
            highCount = 3
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(16)
        assertThat(result.lowCount).isEqualTo(5)
        assertThat(result.mediumCount).isEqualTo(8)
        assertThat(result.highCount).isEqualTo(3)
    }

    @Test
    fun `상 난이도가 전혀 없는 경우 중과 하 난이도에서 문제가 보완된다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 10,
            unitCodes = listOf("uc1564"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH
        )

        val available = ProblemCountPerLevel(
            lowCount = 5,
            mediumCount = 20,
            highCount = 0
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(10)
        assertThat(result.highCount).isEqualTo(0)
        assertThat(result.lowCount + result.mediumCount).isEqualTo(10)
        assertThat(result.mediumCount).isGreaterThan(result.lowCount)
    }

    @Test
    fun `문제가 비율에 나누어 떨어지지 않는 경우 선택한 난이도에서 문제 갯수가 보완된다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 17,
            unitCodes = listOf("uc1571"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.MEDIUM
        )

        val available = ProblemCountPerLevel(
            lowCount = 20,
            mediumCount = 20,
            highCount = 20
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(17)
        assertThat(result.lowCount).isEqualTo(4)
        assertThat(result.mediumCount).isEqualTo(9)
        assertThat(result.highCount).isEqualTo(4)
    }


    @Test
    fun `한 난이도만 가용한 경우 그 난이도에서만 반환된다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 10,
            unitCodes = listOf("uc1568"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.MEDIUM
        )

        val available = ProblemCountPerLevel(
            lowCount = 0,
            mediumCount = 15,
            highCount = 0
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(10)
        assertThat(result.lowCount).isEqualTo(0)
        assertThat(result.mediumCount).isEqualTo(10)
        assertThat(result.highCount).isEqualTo(0)
    }

    @Test
    fun `가용 문제가 전혀 없는 경우 문제를 반환하지 않는다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 10,
            unitCodes = listOf("uc9999"), // 존재하지 않는 unit
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH
        )

        val available = ProblemCountPerLevel(
            lowCount = 0,
            mediumCount = 0,
            highCount = 0
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(0)
        assertThat(result.lowCount).isEqualTo(0)
        assertThat(result.mediumCount).isEqualTo(0)
        assertThat(result.highCount).isEqualTo(0)
    }

    @Test
    fun `요청 문제수가 1개인 경우 선택한 난이도에서 문제가 반환된다`() {
        // Given
        val query = SearchProblemQuery(
            requestCount = 1,
            unitCodes = listOf("uc1523"),
            problemTypes =  listOf(ProblemType.SUBJECTIVE),
            level = ProblemLevel.HIGH
        )

        val available = ProblemCountPerLevel(
            lowCount = 10,
            mediumCount = 10,
            highCount = 10
        )

        // When
        val result = problemLevelCountCalculator.calculateAdaptedCounts(query, available)

        // Then
        assertThat(result.total).isEqualTo(1)
        assertThat(result.highCount).isEqualTo(1)
        assertThat(result.lowCount + result.mediumCount).isEqualTo(0)
    }

}