package com.rxjang.piece.infrastructure.persistance.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.io.Serializable
import java.time.LocalDateTime

@Entity
@Table(name = "piece_problem")
class PieceProblemEntity(
    pieceId: Int,
    problemId: Int,
    order: Int,
) {
    @EmbeddedId
    var id: PieceProblemId = PieceProblemId(pieceId, problemId)
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

    fun changeOrder(order: Int): PieceProblemEntity {
        this.order = order
        return this
    }

}

@Embeddable
data class PieceProblemId(
    @Column(name = "piece_id", nullable = false)
    val pieceId: Int,
    @Column(name = "problem_id", nullable = false)
    val problemId: Int,
) : Serializable