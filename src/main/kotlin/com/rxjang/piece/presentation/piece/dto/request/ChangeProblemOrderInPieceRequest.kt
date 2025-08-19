package com.rxjang.piece.presentation.piece.dto.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated

@Validated
data class ChangeProblemOrderInPieceRequest(
    @field:Size(min = 1, max = 50, message = "문제는 50개 이하여야 합니다.")
    @field:NotEmpty(message = "문제 순서는 비어 있을 수 없습니다.")
    val problemOrders: List<ProblemOrder>
)

data class ProblemOrder(
    val problemId: Int,
    val order: Int,
)
