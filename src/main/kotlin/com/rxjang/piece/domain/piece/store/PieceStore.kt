package com.rxjang.piece.domain.piece.store

import com.rxjang.piece.domain.piece.command.ChangeProblemOrderCommand
import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.problem.model.ProblemId

interface PieceStore {

    fun createPiece(command: CreatePieceCommand): Piece
    fun changeProblemOrder(command: ChangeProblemOrderCommand): List<ProblemId>
}