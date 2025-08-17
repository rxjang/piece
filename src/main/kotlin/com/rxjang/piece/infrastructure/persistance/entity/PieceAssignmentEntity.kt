package com.rxjang.piece.infrastructure.persistance.entity

import com.rxjang.piece.domain.piece.model.AssignmentStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(name = "piece_assignment")
class PieceAssignmentEntity(
    pieceId: Int,
    studentId: Int,
    status: AssignmentStatus = AssignmentStatus.ASSIGNED
) {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
        protected set

    @Column(name = "piece_id", nullable = false)
    var pieceId: Int = pieceId
        protected set

    @Column(name = "student_id", nullable = false)
    var studentId: Int = studentId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: AssignmentStatus = status
        protected set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piece_id", updatable = false, insertable = false)
    var piece: PieceEntity? = null
        protected set

    /**
     * 과제 상태
     */
    fun complete(): PieceAssignmentEntity {
        this.status = AssignmentStatus.COMPLETED
        return this
    }
}