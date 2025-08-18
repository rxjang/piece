package com.rxjang.piece.infrastructure.persistance.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "problem_scoring")
class ProblemScoringEntity(
    pieceAssignmentId: Int,
    problemId: Int,
    studentAnswer: String,
    correctAnswer: String,
    isCorrect: Boolean,
    score: Int = 0
) {
    @EmbeddedId
    var id: ProblemScoringId = ProblemScoringId(pieceAssignmentId, problemId)
        protected set

    @Column(name = "student_answer", nullable = false, length = 1000)
    var studentAnswer: String = studentAnswer
        protected set

    @Column(name = "correct_answer", nullable = false, length = 1000)
    var correctAnswer: String = correctAnswer
        protected set

    @Column(name = "is_correct", nullable = false)
    var isCorrect: Boolean = isCorrect
        protected set

    @Column(name = "score", nullable = false)
    var score: Int = score
        protected set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_assignment_id", updatable = false, insertable = false)
    var pieceAssignment: PieceAssignmentEntity? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", updatable = false, insertable = false)
    var problem: ProblemEntity? = null
        protected set
}

@Embeddable
data class ProblemScoringId(
    @Column(name = "piece_assignment_id", nullable = false)
    val pieceAssignmentId: Int,
    @Column(name = "problem_id", nullable = false)
    val problemId: Int,
) : Serializable