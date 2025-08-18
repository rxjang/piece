package com.rxjang.piece.infrastructure.persistance.repository

import com.rxjang.piece.infrastructure.persistance.entity.ProblemScoringEntity
import com.rxjang.piece.infrastructure.persistance.entity.ProblemScoringId
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemScoreRepository: JpaRepository<ProblemScoringEntity, ProblemScoringId>