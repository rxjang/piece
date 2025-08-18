package com.rxjang.piece.infrastructure.persistance.repository.custom

import com.rxjang.piece.domain.piece.model.ProblemStatistic

interface ProblemScoringRepositoryCustom {

    fun getProblemStatisticsByPieceId(pieceId: Int): List<ProblemStatistic>

}