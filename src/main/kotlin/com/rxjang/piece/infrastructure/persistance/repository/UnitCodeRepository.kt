package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.UnitCodeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UnitCodeRepository: JpaRepository<UnitCodeEntity, Int> {
}