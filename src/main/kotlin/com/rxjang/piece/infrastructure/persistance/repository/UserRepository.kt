package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.UserEntity
import com.rxjang.piece.infrastructure.persistance.entity.UserStatus
import com.rxjang.piece.infrastructure.persistance.entity.UserType
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, Int> {

    fun findByIdIn(ids: List<Int>): List<UserEntity>
    fun findByIdInAndUserTypeAndStatus(
        ids: List<Int>,
        userType: UserType,
        statue: UserStatus = UserStatus.ACTIVE,
    ): List<UserEntity>
}