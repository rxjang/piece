package com.rxjang.piece.infrastructure.persistance.entity

import com.rxjang.piece.domain.problem.model.ProblemType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "problem")
class ProblemEntity(
    unitCode: String,
    level: Int,
    type: ProblemType,
    answer: String,
) {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
        protected set

    @Column(name = "unit_code", nullable = false)
    var unitCode: String = unitCode
        protected set

    @Column(name = "level", nullable = false)
    var level: Int = level
        protected set

    @Column(name = "problem_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var type: ProblemType = type
        protected set

    @Column(name = "answer", nullable = false)
    var answer: String = answer
        protected set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

}