package com.rxjang.piece.infrastructure.persistance.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "piece")
class PieceEntity(
    userId: Int,
    title: String,
) {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
        protected set

    @Column(name = "user_id", nullable = false)
    var userId: Int = userId
        protected set

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(mappedBy = "piece")
    var problems: MutableList<PieceProblemEntity> = mutableListOf()
        protected set
}