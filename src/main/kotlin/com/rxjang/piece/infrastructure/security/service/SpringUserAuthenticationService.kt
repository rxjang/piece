package com.rxjang.piece.infrastructure.security.service

import com.rxjang.piece.domain.auth.model.AuthFailureReason
import com.rxjang.piece.domain.auth.model.AuthenticationResult
import com.rxjang.piece.domain.auth.model.UserCredentials
import com.rxjang.piece.domain.auth.service.UserAuthenticationService
import com.rxjang.piece.infrastructure.security.principal.CustomUserPrincipal
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class SpringUserAuthenticationService(
    private val authenticationManager: AuthenticationManager,
) : UserAuthenticationService {

    override fun authenticateUser(credentials: UserCredentials): AuthenticationResult {
        try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(credentials.username, credentials.password)
            )
            val userPrincipal = authentication.principal as CustomUserPrincipal
            val domainUser = userPrincipal.getDomainUser()
            return AuthenticationResult.Success(domainUser)
        } catch (e: BadCredentialsException) {
            return AuthenticationResult.Failure(AuthFailureReason.INVALID_CREDENTIALS)
        } catch (e: UsernameNotFoundException) {
            return AuthenticationResult.Failure(AuthFailureReason.USER_NOT_FOUND)
        }
    }
}