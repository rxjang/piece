package com.rxjang.piece.infrastructure.security.service

import com.rxjang.piece.application.common.UserFailureCode
import com.rxjang.piece.infrastructure.persistance.repository.UserRepository
import com.rxjang.piece.infrastructure.security.principal.CustomUserPrincipal
import com.rxjang.piece.presentation.exception.BusinessException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameAndStatus(username)
            ?: throw BusinessException(UserFailureCode.CANNOT_FIND_USER)

        return CustomUserPrincipal(user)
    }
}