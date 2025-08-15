package com.rxjang.piece.domain.user.model

sealed interface User {
    val id: UserId
    val name: String
}

sealed interface UserId