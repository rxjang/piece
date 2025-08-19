package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.ProblemId

data class ScorePieceCommand(
    val pieceId: PieceId,
    val answers: List<ProblemWithAnswer>

)

data class ProblemWithAnswer(
    val problemId: ProblemId,
    val answer: String,
)
