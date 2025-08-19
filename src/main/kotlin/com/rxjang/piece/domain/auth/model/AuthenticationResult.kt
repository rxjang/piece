package com.rxjang.piece.domain.auth.model

import com.rxjang.piece.domain.user.model.User

sealed interface AuthenticationResult {
    data class Success(val user: User) : AuthenticationResult
    data class Failure(val reason: AuthFailureReason) : AuthenticationResult
}

enum class AuthFailureReason(val message: String) {
    INVALID_CREDENTIALS("잘못된 인증 정보입니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
}
