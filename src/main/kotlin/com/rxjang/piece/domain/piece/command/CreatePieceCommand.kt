package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.TeacherId

data class CreatePieceCommand(
    val teacherId: TeacherId,
    val title: String,
    val problemIds: Set<ProblemId>
) {
    init {
        require(problemIds.isNotEmpty()) {
            "문제는 비어있을 수 없습니다."
        }
        require(problemIds.size <= 50) {
            "문제는 최대 50개 까지만 등록이 가능합니다."
        }
    }
}
