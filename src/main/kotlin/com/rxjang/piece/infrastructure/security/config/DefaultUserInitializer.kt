package com.rxjang.piece.infrastructure.security.config

import com.rxjang.piece.infrastructure.persistance.entity.UserEntity
import com.rxjang.piece.domain.user.model.UserType
import com.rxjang.piece.infrastructure.persistance.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class DefaultUserInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostConstruct
    fun initDefaultUsers() {
        if (userRepository.count() == 0L) {
            logger.info { "기본 사용자 데이터 생성" }
            createDefaultUsers()
            logger.info { "기본 사용자 데이터 생성이 완료" }
        } else {
            logger.info { "사용자 데이터가 이미 존재합니다. 초기화를 건너뜁니다." }
        }
    }

    private fun createDefaultUsers() {
        val defaultUsers = listOf(
            // 기본 선생님
            UserEntity(
                username = "teacher1",
                password = passwordEncoder.encode("password123"),
                name = "김선생",
                userType = UserType.TEACHER
            ),
            UserEntity(
                username = "teacher2",
                password = passwordEncoder.encode("password123"),
                name = "이선생",
                userType = UserType.TEACHER
            ),
            // 기본 학생들
            UserEntity(
                username = "student1",
                password = passwordEncoder.encode("password123"),
                name = "홍길동",
                userType = UserType.STUDENT
            ),
            UserEntity(
                username = "student2",
                password = passwordEncoder.encode("password123"),
                name = "김철수",
                userType = UserType.STUDENT
            ),
            UserEntity(
                username = "student3",
                password = passwordEncoder.encode("password123"),
                name = "이영희",
                userType = UserType.STUDENT
            )
        )

        userRepository.saveAll(defaultUsers)

        // 로그로 생성된 사용자 정보 출력
        defaultUsers.forEach { user ->
            logger.info { "생성된 사용자: ${user.userType} - ${user.username} (${user.name})" }
        }
    }
}