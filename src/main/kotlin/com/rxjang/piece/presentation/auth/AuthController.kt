package com.rxjang.piece.presentation.auth

import com.rxjang.piece.infrastructure.security.jwt.JwtTokenProvider
import com.rxjang.piece.infrastructure.security.principal.CustomUserPrincipal
import com.rxjang.piece.presentation.auth.request.LoginRequest
import com.rxjang.piece.presentation.auth.response.LoginResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        val userPrincipal = authentication.principal as CustomUserPrincipal
        val token = jwtTokenProvider.generateToken(userPrincipal)

        return ResponseEntity.ok(
            LoginResponse(
                token = token,
                userId = userPrincipal.getUserId(),
                username = userPrincipal.username,
                name = userPrincipal.getName(),
                userType = userPrincipal.getUserType().name,
            )
        )
    }
}