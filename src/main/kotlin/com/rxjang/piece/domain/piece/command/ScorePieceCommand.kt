package com.rxjang.piece.domain.piece.command

import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.StudentId

data class ScorePieceCommand(
    val pieceId: PieceId,
    val studentId: StudentId,
    val answers: List<ProblemWithAnswer>

)

data class ProblemWithAnswer(
    val problemId: ProblemId,
    val answer: String,
)
