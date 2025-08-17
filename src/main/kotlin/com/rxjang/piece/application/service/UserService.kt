package com.rxjang.piece.application.service

import com.rxjang.piece.domain.user.model.StudentId
import org.springframework.stereotype.Service

@Service
class UserService(

) {

    fun findStudentById(ids: List<StudentId>): List<StudentId> {
        // TODO 학생 찾는 로직
        return ids
    }
}