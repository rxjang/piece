package com.rxjang.piece.domain.auth.service

import com.rxjang.piece.domain.auth.model.AuthToken
import com.rxjang.piece.domain.user.model.User

interface TokenService {
    fun generateToken(user: User): AuthToken
}