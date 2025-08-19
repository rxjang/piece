package com.rxjang.piece.domain.auth.model


@JvmInline
value class AuthToken(val value: String) {
    init {
        require(value.isNotBlank()) { "토큰은 비어있을 수 없습니다." }
    }
}