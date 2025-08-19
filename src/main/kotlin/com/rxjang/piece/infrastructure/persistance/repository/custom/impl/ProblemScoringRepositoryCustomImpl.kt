package com.rxjang.piece.infrastructure.persistance.repository.custom.impl

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rxjang.piece.domain.piece.model.ProblemStatistic
import com.rxjang.piece.infrastructure.persistance.entity.QPieceAssignmentEntity.pieceAssignmentEntity
import com.rxjang.piece.infrastructure.persistance.entity.QPieceProblemEntity.pieceProblemEntity
import com.rxjang.piece.infrastructure.persistance.entity.QProblemEntity.problemEntity
import com.rxjang.piece.infrastructure.persistance.entity.QProblemScoringEntity.problemScoringEntity
import com.rxjang.piece.infrastructure.persistance.repository.custom.ProblemScoringRepositoryCustom
import org.springframework.stereotype.Repository

@Repository
class ProblemScoringRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ProblemScoringRepositoryCustom {

    override fun getProblemStatisticsByPieceId(pieceId: Int): List<ProblemStatistic> {

        // 서브쿼리: 해당 pieceId에 속한 assignment들의 ID 조회
        val assignmentIdsSubQuery = JPAExpressions.select(pieceAssignmentEntity.id)
            .from(pieceAssignmentEntity)
            .where(pieceAssignmentEntity.pieceId.eq(pieceId))

        val results = queryFactory
            .select(
                Projections.constructor(
                    ProblemStatistic::class.java,
                    problemEntity.id,
                    problemScoringEntity.count().intValue(),
                    problemScoringEntity.isCorrect.`when`(true).then(1).otherwise(0).sum().intValue(),
                )
            )
            .from(pieceProblemEntity)
            .join(problemEntity).on(pieceProblemEntity.id.problemId.eq(problemEntity.id))
            .leftJoin(problemScoringEntity).on(
                pieceProblemEntity.id.problemId.eq(problemScoringEntity.id.problemId)
                    .and(problemScoringEntity.id.pieceAssignmentId.`in`(assignmentIdsSubQuery))
            )
            .where(pieceProblemEntity.id.pieceId.eq(pieceId))
            .groupBy(problemEntity.id, pieceProblemEntity.order)
            .orderBy(pieceProblemEntity.order.asc())
            .fetch()

        return results
    }
}