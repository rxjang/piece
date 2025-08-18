package com.rxjang.piece.application.service

import com.rxjang.piece.domain.user.model.Student
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.reader.UserReader
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userReader: UserReader,
) {

    fun findStudentById(ids: List<StudentId>): List<Student> {
        return userReader.findActiveStudentsById(ids)
    }
}