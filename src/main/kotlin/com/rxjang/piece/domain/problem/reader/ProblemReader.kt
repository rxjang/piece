package com.rxjang.piece.domain.problem.reader

import com.rxjang.piece.application.dto.ProblemCountPerLevel
import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.Problem
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.problem.model.ProblemType

interface ProblemReader {

    fun getOrderedProblemsByIds(problemIds: List<ProblemId>): List<Problem>

    fun countProblemByLevel(unitCodes: List<String>, types: List<ProblemType>): ProblemCountPerLevel

    fun searchProblems(query: SearchProblemQuery, calculatedCount: ProblemCountPerLevel): List<Problem>
}