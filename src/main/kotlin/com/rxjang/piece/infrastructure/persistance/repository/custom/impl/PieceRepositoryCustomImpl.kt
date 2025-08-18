package com.rxjang.piece.infrastructure.persistance.repository.custom.impl

import com.querydsl.jpa.impl.JPAQueryFactory
import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity
import com.rxjang.piece.infrastructure.persistance.entity.QPieceAssignmentEntity.pieceAssignmentEntity
import com.rxjang.piece.infrastructure.persistance.entity.QPieceEntity.pieceEntity
import com.rxjang.piece.infrastructure.persistance.entity.QPieceProblemEntity.pieceProblemEntity
import com.rxjang.piece.infrastructure.persistance.entity.QProblemEntity.problemEntity
import com.rxjang.piece.infrastructure.persistance.repository.custom.PieceRepositoryCustom
import org.springframework.stereotype.Repository

@Repository
class PieceRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
): PieceRepositoryCustom {

    override fun findProblemsInPieceForStudent(
        pieceId: Int,
        studentId: Int
    ): List<ProblemEntity> {
        return queryFactory
            .selectFrom(problemEntity)
            .join(pieceProblemEntity).on(pieceProblemEntity.problem.eq(problemEntity))
            .join(pieceProblemEntity.piece, pieceEntity)
            .join(pieceAssignmentEntity).on(pieceAssignmentEntity.piece.eq(pieceEntity))
            .where(
                pieceEntity.id.eq(pieceId),
                pieceAssignmentEntity.studentId.eq(studentId)
            )
            .orderBy(pieceProblemEntity.order.asc())
            .fetch()
    }
}