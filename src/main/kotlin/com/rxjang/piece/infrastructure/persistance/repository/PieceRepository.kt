package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PieceRepository: JpaRepository<PieceEntity, Int> {
}