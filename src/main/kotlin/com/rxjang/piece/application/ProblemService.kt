package com.rxjang.piece.application

import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.reader.ProblemReader
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class ProblemService(
    private val problemReader: ProblemReader,
    private val problemLevelCountCalculator: ProblemLevelCountCalculator,
) {

    @Transactional(readOnly = true)
    fun searchProblems(query: SearchProblemQuery): List<Problem> {
        val availableProblemCounts = problemReader.countProblemByLevel(query.unitCodes, query.problemType)
        logger.debug { "[문제 조회] 요청 문제수: ${query.requestCount} 실 가용 문제수: $availableProblemCounts" }

        val calculated = problemLevelCountCalculator.calculateAdaptedCounts(query, availableProblemCounts)
        logger.debug { "[문제 조회] 비율에 맞춘 문항 수: $calculated" }

        return problemReader.searchProblems(query, calculated)
    }

}