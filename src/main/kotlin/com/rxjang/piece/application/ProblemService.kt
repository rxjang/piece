package com.rxjang.piece.application

import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.reader.ProblemReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemService(
    private val problemReader: ProblemReader,
    private val problemLevelCountCalculator: ProblemLevelCountCalculator,
) {

    @Transactional(readOnly = true)
    fun searchProblems(query: SearchProblemQuery): List<Problem> {
        // 실제 가용 문제 수
        val availableProblemCounts = problemReader.countProblemByLevel(query.unitCodes, query.problemType)
        // 난이도 비율에 맞춘 문제 수 계산
        val calculated = problemLevelCountCalculator.calculateAdaptedCounts(query, availableProblemCounts)
        return problemReader.searchProblems(query, calculated)
    }

}