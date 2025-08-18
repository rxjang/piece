package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.PieceAssignmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PieceAssignmentRepository: JpaRepository<PieceAssignmentEntity, Int> {

    fun findByPieceIdAndStudentIdIn(pieceId: Int, studentIds: List<Int>): List<PieceAssignmentEntity>

    fun findByPieceIdAndStudentId(pieceId: Int, studentId: Int): PieceAssignmentEntity?
}