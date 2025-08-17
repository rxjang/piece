package com.rxjang.piece.infrastructure.persistance.reader

import com.rxjang.piece.application.dto.ProblemCountPerLevel
import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.domain.problem.reader.ProblemReader
import com.rxjang.piece.infrastructure.persistance.mapper.ProblemMapper.toModel
import com.rxjang.piece.infrastructure.persistance.repository.ProblemRepository
import org.springframework.stereotype.Repository

@Repository
class ProblemReaderImpl(
    private val problemRepository: ProblemRepository,
): ProblemReader {

    override fun countProblemByLevel(unitCodes: List<String>, type: ProblemType): ProblemCountPerLevel {
        return problemRepository.getProblemCountsByLevel(unitCodes, type)
    }

    override fun searchProblems(query: SearchProblemQuery, calculatedCount: ProblemCountPerLevel): List<Problem> {
        return ProblemLevel.entries.flatMap {
            val requestCount = when (it) {
                ProblemLevel.LOW -> calculatedCount.lowCount
                ProblemLevel.MEDIUM -> calculatedCount.mediumCount
                ProblemLevel.HIGH -> calculatedCount.highCount
            }
            if (requestCount == 0) return@flatMap emptyList()
            problemRepository
                .searchProblems(query.unitCodes, query.problemType, it.valueRange.toList(), requestCount)
                .map { problem -> problem.toModel() }
        }
    }
}