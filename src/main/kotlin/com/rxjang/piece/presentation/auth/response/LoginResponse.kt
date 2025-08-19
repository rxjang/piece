package com.rxjang.piece.presentation.auth.response

data class LoginResponse(
    val token: String,
    val userId: Int,
    val username: String,
    val name: String,
    val userType: String,
)
