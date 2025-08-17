package com.rxjang.piece.infrastructure.persistance.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "piece_problem")
class PieceProblemEntity(
    pieceId: Int,
    problemId: Int,
    order: Int,
) {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
        protected set

    @Column(name = "piece_id", nullable = false)
    var pieceId: Int = pieceId
        protected set

    @Column(name = "piece_problem_id", nullable = false)
    var problemId: Int = problemId
        protected set

    @Column(name = "problem_order", nullable = false)
    var order: Int = order
        protected set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id", updatable = false, insertable = false)
    var piece: PieceEntity? = null
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", updatable = false, insertable = false)
    var problem: ProblemEntity? = null
        protected set

}