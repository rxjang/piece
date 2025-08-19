package com.rxjang.piece.infrastructure.persistance.reader

import com.rxjang.piece.domain.user.model.Student
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.reader.UserReader
import com.rxjang.piece.domain.user.model.UserType
import com.rxjang.piece.infrastructure.persistance.mapper.UserMapper.toStudent
import com.rxjang.piece.infrastructure.persistance.repository.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserReaderImpl(
    private val userRepository: UserRepository,
): UserReader {

    override fun findActiveStudentsById(ids: List<StudentId>): List<Student>  {
        return userRepository.findByIdInAndUserTypeAndStatus(ids.map { it.value }, UserType.STUDENT)
            .map { it.toStudent() }
    }
}