package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity
import com.rxjang.piece.infrastructure.persistance.repository.custom.ProblemRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepository: JpaRepository<ProblemEntity, Int>, ProblemRepositoryCustom {

}