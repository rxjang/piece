package com.rxjang.piece.application.dto

import com.rxjang.piece.domain.piece.model.PieceAssignmentId
import com.rxjang.piece.domain.problem.model.ProblemId

data class SaveScoredAnswerCommand(
    val pieceAssignmentId: PieceAssignmentId,
    val problemId: ProblemId,
    val studentAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val score: Int,
)