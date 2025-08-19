package com.rxjang.piece.domain.piece.store

import com.rxjang.piece.application.dto.SaveScoredAnswerCommand
import com.rxjang.piece.application.dto.UpdatePieceAssignmentCommand
import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.command.ChangeProblemOrderCommand
import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceAssignment
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.TeacherId

interface PieceStore {

    fun createPiece(command: CreatePieceCommand, teacherId: TeacherId): Piece
    fun changeProblemOrder(command: ChangeProblemOrderCommand): List<ProblemId>
    fun assignToStudent(command: AssignPieceCommand): List<PieceAssignment>
    fun saveScoringPieceResult(commands: List<SaveScoredAnswerCommand>)
    fun updateAssignment(command: UpdatePieceAssignmentCommand)
}