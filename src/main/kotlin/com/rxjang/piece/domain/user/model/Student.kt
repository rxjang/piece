package com.rxjang.piece.domain.user.model

import com.rxjang.piece.infrastructure.persistance.entity.UserStatus
import java.time.LocalDateTime

data class Student(
    override val id: StudentId,
    override val name: String,
    override val username: String,
    override val status: UserStatus,
    override val createdAt: LocalDateTime,
): User

@JvmInline
value class StudentId(val value: Int): UserId
