package com.rxjang.piece.application.dto.response

data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String,
    val name: String,
    val userType: String,
)