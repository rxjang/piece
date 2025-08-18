package com.rxjang.piece.domain.user.model

import com.rxjang.piece.infrastructure.persistance.entity.UserStatus
import java.time.LocalDateTime

sealed interface User {
    val id: UserId
    val name: String
    val username: String
    val status: UserStatus
    val createdAt: LocalDateTime
}

sealed interface UserId