package com.rxjang.piece.presentation.piece.dto.request

import jakarta.validation.constraints.NotEmpty
import org.springframework.validation.annotation.Validated

@Validated
data class ScorePieceRequest(
    val studentId: Int,
    @field:NotEmpty(message = "답변은 비어 있을 수 없습니다")
    val answers: List<ProblemWithAnswerRequest>
)

data class ProblemWithAnswerRequest(
    val problemId: Int,
    val answer: String,
)