package com.rxjang.piece.domain.user.model

data class Teacher(
    override val id: TeacherId,
    override val name: String,
): User

@JvmInline
value class TeacherId(val value: Int): UserId
