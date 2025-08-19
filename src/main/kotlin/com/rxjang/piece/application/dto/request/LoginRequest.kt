package com.rxjang.piece.application.dto.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "사용자명을 입력해주세요.")
    val username: String,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String
)