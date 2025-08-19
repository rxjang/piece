package com.rxjang.piece.application.exception.codes

enum class AuthFailureCode(
    override val code: String,
    override val message: String
) : FailureCode {
    INVALID_TOKEN("001", "유효하지 않은 토큰입니다."),
    INVALID_CREDENTIALS("002", "아이디 또는 비밀번호가 올바르지 않습니다."),
    ;

    override val prefix: String = "AUTH"
}