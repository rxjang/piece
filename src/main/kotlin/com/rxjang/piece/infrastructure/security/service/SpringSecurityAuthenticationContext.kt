package com.rxjang.piece.infrastructure.security.service

import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.domain.user.model.UserType
import com.rxjang.piece.domain.auth.service.AuthenticationContext
import com.rxjang.piece.infrastructure.security.principal.CustomUserPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpringSecurityAuthenticationContext: AuthenticationContext {

    private fun getCurrentUser(): CustomUserPrincipal? {
        val authentication = SecurityContextHolder.getContext().authentication
        return if (authentication?.principal is CustomUserPrincipal) {
            authentication.principal as CustomUserPrincipal
        } else null
    }

    override fun getCurrentTeacherId(): TeacherId {
        return getCurrentUser()?.getTeacherId()
            ?: throw IllegalStateException("교사 권한이 필요합니다.")
    }

    override fun getCurrentStudentId(): StudentId {
        return getCurrentUser()?.getStudentId()
            ?: throw IllegalStateException("교사 권한이 필요합니다.")
    }

    override fun getCurrentUserId(): Int {
        return getCurrentUser()?.getUserId()
            ?: throw IllegalStateException("인증된 사용자가 없습니다.")
    }

    override fun getCurrentUserType(): UserType {
        return getCurrentUser()?.getUserType()
            ?: throw IllegalStateException("인증된 사용자가 없습니다.")
    }

    override fun isTeacher(): Boolean {
        return getCurrentUserType() == UserType.TEACHER
    }

    override fun isStudent(): Boolean {
        return getCurrentUserType() == UserType.STUDENT
    }


}