package com.rxjang.piece.domain.user.model

data class Student(
    override val id: StudentId,
    override val name: String,
): User

@JvmInline
value class StudentId(val value: Int): UserId
