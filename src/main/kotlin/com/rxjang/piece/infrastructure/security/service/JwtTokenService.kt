package com.rxjang.piece.infrastructure.security.service

import com.rxjang.piece.domain.auth.model.AuthToken
import com.rxjang.piece.domain.user.model.Student
import com.rxjang.piece.domain.user.model.Teacher
import com.rxjang.piece.domain.user.model.User
import com.rxjang.piece.domain.user.model.UserType
import com.rxjang.piece.domain.auth.service.TokenService
import com.rxjang.piece.infrastructure.persistance.entity.UserEntity
import com.rxjang.piece.infrastructure.security.jwt.JwtTokenProvider
import com.rxjang.piece.infrastructure.security.principal.CustomUserPrincipal
import org.springframework.stereotype.Component

@Component
class JwtTokenService(
    private val jwtTokenProvider: JwtTokenProvider,
) : TokenService {

    override fun generateToken(user: User): AuthToken {
        val userPrincipal = user.toCustomUserPrincipal()
        val jwtToken = jwtTokenProvider.generateToken(userPrincipal)
        return AuthToken(jwtToken)
    }

    fun User.toCustomUserPrincipal(): CustomUserPrincipal {
        val userId = when (this) {
            is Student -> this.id.value
            is Teacher -> this.id.value
        }
        val userEntity = UserEntity(
            username = this.username,
            password = "",
            name = this.name,
            userType = when (this) {
                is Teacher -> UserType.TEACHER
                is Student -> UserType.STUDENT
            }
        ).apply { id =  userId }
        return CustomUserPrincipal(userEntity)
    }
}
