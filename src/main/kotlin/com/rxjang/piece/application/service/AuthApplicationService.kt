package com.rxjang.piece.application.service

import com.rxjang.piece.application.dto.request.LoginRequest
import com.rxjang.piece.application.dto.response.LoginResponse
import com.rxjang.piece.application.exception.BusinessException
import com.rxjang.piece.application.exception.codes.AuthFailureCode
import com.rxjang.piece.domain.auth.model.AuthenticationResult
import com.rxjang.piece.domain.user.model.Student
import com.rxjang.piece.domain.user.model.Teacher
import com.rxjang.piece.domain.auth.model.UserCredentials
import com.rxjang.piece.domain.user.model.UserType
import com.rxjang.piece.domain.auth.service.TokenService
import com.rxjang.piece.domain.auth.service.UserAuthenticationService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthApplicationService(
    private val userAuthenticationService: UserAuthenticationService,
    private val tokenService: TokenService,
) {

    fun login(request: LoginRequest): LoginResponse {
        val credentials = UserCredentials(request.username, request.password)
        val authResult = userAuthenticationService.authenticateUser(credentials)

        return when (authResult) {
            is AuthenticationResult.Success -> {
                val token = tokenService.generateToken(authResult.user)
                when (authResult.user) {
                    is Student -> LoginResponse(
                        token = token.value,
                        userId = authResult.user.id.value,
                        userType = UserType.STUDENT.name,
                        username = authResult.user.username,
                        name = authResult.user.name,
                    )
                    is Teacher -> LoginResponse(
                        token = token.value,
                        userId = authResult.user.id.value,
                        userType = UserType.TEACHER.name,
                        username = authResult.user.username,
                        name = authResult.user.name,
                    )
                }

            }
            is AuthenticationResult.Failure -> {
                throw BusinessException(AuthFailureCode.INVALID_CREDENTIALS)
            }
        }
    }
}