package com.rxjang.piece.presentation.auth

import com.rxjang.piece.application.dto.request.LoginRequest
import com.rxjang.piece.application.dto.response.LoginResponse
import com.rxjang.piece.application.service.AuthApplicationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authApplicationService: AuthApplicationService,
) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authApplicationService.login(request)
        return ResponseEntity.ok(response)
    }
}