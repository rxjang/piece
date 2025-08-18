package com.rxjang.piece.domain.piece.model

import com.rxjang.piece.domain.user.model.StudentId

data class PieceStatistics(
    val pieceId: PieceId,
    val title: String,
    val totalAssignments: Int = 0,
    val completedAssignments: Int = 0,
    val averageScore: Double = 0.0,
    val studentStatistics: List<StudentStatistic> = emptyList(),
    val problemStatistics: List<ProblemStatistic> = emptyList(),
)

data class StudentStatistic(
    val studentId: StudentId,
    val status: AssignmentStatus,
    val score: Int = 0,
    val correctRate: Double = 0.0,
)

data class ProblemStatistic(
    val problemId: Int,
    val totalAttempts: Int,
    val correctAnswers: Int,
) {
    val correctRate = correctAnswers / totalAttempts * 100.0
}
