package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.TeacherId

data class ChangeProblemOrderCommand(
    val pieceId: PieceId,
    val teacherId: TeacherId,
    val problemOrders: List<ProblemOrder>,
) {

    fun validateOrder(): Boolean {
        val orders = problemOrders.map { it.order }.sorted()

        // 1부터 시작하는지 확인
        if (orders.first() != 1) return false

        // 연속적으로 1씩 증가하는지 확인
        return orders.zipWithNext().all { (current, next) -> next == current + 1 }
    }
}

data class ProblemOrder(
    val problemId: ProblemId,
    val order: Int,
)
