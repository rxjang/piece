package com.rxjang.piece.domain.user.service

import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.domain.user.model.UserType

interface AuthenticationContext {
    fun getCurrentTeacherId(): TeacherId
    fun getCurrentStudentId(): StudentId
    fun getCurrentUserId(): Int
    fun getCurrentUserType(): UserType
    fun isTeacher(): Boolean
    fun isStudent(): Boolean
}