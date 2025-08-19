package com.rxjang.piece.infrastructure.security.principal

import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.domain.user.model.UserType
import com.rxjang.piece.infrastructure.persistance.entity.UserEntity
import com.rxjang.piece.infrastructure.persistance.entity.UserStatus
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserPrincipal(
    private val user: UserEntity,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.userType.name}"))
    }

    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.username
    override fun isAccountNonExpired(): Boolean = user.status == UserStatus.ACTIVE
    override fun isAccountNonLocked(): Boolean = user.status == UserStatus.ACTIVE
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = user.status == UserStatus.ACTIVE

    fun getUserId(): Int = user.id!!
    fun getUserType(): UserType = user.userType
    fun getName(): String = user.name

    fun getTeacherId(): TeacherId? = if (user.userType == UserType.TEACHER) TeacherId(user.id!!) else null
    fun getStudentId(): StudentId? = if (user.userType == UserType.STUDENT) StudentId(user.id!!) else null
}