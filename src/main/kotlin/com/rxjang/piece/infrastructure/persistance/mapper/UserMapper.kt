package com.rxjang.piece.infrastructure.persistance.mapper

import com.rxjang.piece.domain.user.model.Student
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.Teacher
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.infrastructure.persistance.entity.UserEntity
import com.rxjang.piece.infrastructure.persistance.entity.UserType

object UserMapper {

    fun UserEntity.toTeacher(): Teacher {
        require(this.userType == UserType.TEACHER) { "Not a teacher" }
        return Teacher(
            id = TeacherId(this.id!!),
            name = this.name,
            username = this.username,
            status = this.status,
            createdAt = this.createdAt
        )
    }

    fun UserEntity.toStudent(): Student {
        require(this.userType == UserType.STUDENT) { "Not a student" }
        return Student(
            id = StudentId(this.id!!),
            name = this.name,
            username = this.username,
            status = this.status,
            createdAt = this.createdAt
        )
    }
}