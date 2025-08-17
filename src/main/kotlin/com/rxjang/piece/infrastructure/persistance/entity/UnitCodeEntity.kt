package com.rxjang.piece.infrastructure.persistance.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "unit_code")
class UnitCodeEntity(
    unitCode: String,
    name: String,
) {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var index: Int? = null
        protected set

    @Column(name = "unit_code", nullable = false, unique = true)
    var unitCode: String = unitCode
        protected set

    @Column(name = "name", nullable = false, length = 500)
    var name: String = name
        protected set

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

}