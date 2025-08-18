package com.rxjang.piece.domain.user.reader

import com.rxjang.piece.domain.user.model.Student
import com.rxjang.piece.domain.user.model.StudentId

interface UserReader {

    fun findActiveStudentsById(ids: List<StudentId>): List<Student>
}