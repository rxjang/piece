package com.rxjang.piece.domain.auth.service

import com.rxjang.piece.domain.auth.model.AuthenticationResult
import com.rxjang.piece.domain.auth.model.UserCredentials

interface UserAuthenticationService {
    fun authenticateUser(credentials: UserCredentials): AuthenticationResult
}