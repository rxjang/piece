package com.rxjang.piece.infrastructure.persistance.repository.custom.impl

import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions.nullExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rxjang.piece.domain.problem.dto.ProblemCountPerLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.infrastructure.persistance.entity.ProblemEntity
import com.rxjang.piece.infrastructure.persistance.entity.QProblemEntity.problemEntity
import com.rxjang.piece.infrastructure.persistance.entity.QUnitCodeEntity.unitCodeEntity
import com.rxjang.piece.infrastructure.persistance.repository.custom.ProblemRepositoryCustom
import org.springframework.stereotype.Repository


@Repository
class ProblemRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
): ProblemRepositoryCustom {

    override fun getProblemCountsByLevel(
        unitCodes: List<String>,
        types: List<ProblemType>,
    ): ProblemCountPerLevel {
        val result = queryFactory
            .select(
        CaseBuilder()
                    .`when`(problemEntity.level.eq(1))
                    .then(1).otherwise(nullExpression())
                    .count().`as`("low"),
                CaseBuilder()
                    .`when`(problemEntity.level.`in`(2, 3, 4))
                    .then(1).otherwise(nullExpression())
                    .count().`as`("medium"),
                CaseBuilder()
                    .`when`(problemEntity.level.eq(5))
                    .then(1).otherwise(nullExpression())
                    .count().`as`("high"),
            )
            .from(problemEntity)
            .join(unitCodeEntity).on(problemEntity.unitCode.eq(unitCodeEntity.unitCode))
            .where(
                problemEntity.unitCode.`in`(unitCodes)
                    .and(problemEntity.type.`in`(types))
            )
            .fetchOne()

        return ProblemCountPerLevel(
            lowCount = result?.get(0, Long::class.java)?.toInt() ?: 0,
            mediumCount = result?.get(1, Long::class.java)?.toInt() ?: 0,
            highCount = result?.get(2, Long::class.java)?.toInt() ?: 0,
        )
    }

    override fun searchProblems(
        unitCodes: List<String>,
        types: List<ProblemType>,
        levels: List<Int>,
        requestCount: Int,
    ): List<ProblemEntity> {
        return queryFactory
            .selectFrom(problemEntity)
            .join(unitCodeEntity).on(problemEntity.unitCode.eq(unitCodeEntity.unitCode))
            .where(
                problemEntity.unitCode.`in`(unitCodes)
                    .and(problemEntity.type.`in`(types))
                    .and(problemEntity.level.`in`(levels))
            )
            .orderBy(
                problemEntity.unitCode.asc(),
                problemEntity.type.asc(),
                problemEntity.level.asc()
            )
            .limit(requestCount.toLong())
            .fetch()
    }

}