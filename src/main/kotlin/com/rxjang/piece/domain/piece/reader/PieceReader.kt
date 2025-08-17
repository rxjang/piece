package com.rxjang.piece.domain.piece.reader

import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId

interface PieceReader {

    fun findById(id: PieceId): Piece?
}