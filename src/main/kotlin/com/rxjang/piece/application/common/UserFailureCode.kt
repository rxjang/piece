package com.rxjang.piece.application.common

enum class UserFailureCode(
    override val code: String,
    override val message: String
) : FailureCode {
    SOME_USER_NOT_FOUND("001", "존재하지 않는 사용자가 있습니다."),
    ;

    override val prefix: String = "USER"
}