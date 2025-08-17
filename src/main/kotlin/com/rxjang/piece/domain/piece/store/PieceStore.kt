package com.rxjang.piece.domain.piece.store

import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.model.Piece

interface PieceStore {

    fun createPiece(command: CreatePieceCommand): Piece
}