package com.rxjang.piece.infrastructure.persistance.reader

import com.rxjang.piece.application.dto.ProblemCountPerLevel
import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.model.ProblemId
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

    override fun getOrderedProblemsByIds(problemIds: List<ProblemId>): List<Problem> {
        return problemRepository.findByIdInOrderByUnitCodeAscLevelAsc(problemIds.map { it.value })
            .map { it.toModel() }
    }

    override fun countProblemByLevel(unitCodes: List<String>, types: List<ProblemType>): ProblemCountPerLevel {
        return problemRepository.getProblemCountsByLevel(unitCodes, types)
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
                .searchProblems(query.unitCodes, query.problemTypes, it.valueRange.toList(), requestCount)
                .map { problem -> problem.toModel() }
        }
    }
}