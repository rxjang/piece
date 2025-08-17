package com.rxjang.piece.presentation.problem.dto.request

import com.rxjang.piece.domain.problem.model.ProblemType

enum class ProblemRequestType {
    ALL,
    SUBJECTIVE,
    SELECTION,
    ;

    fun toDomainEnum(): List<ProblemType> {
        return when (this) {
            ALL -> listOf(ProblemType.SUBJECTIVE, ProblemType.SELECTION)
            SUBJECTIVE -> listOf(ProblemType.SELECTION)
            SELECTION -> listOf(ProblemType.SELECTION)
        }
    }
}