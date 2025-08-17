package com.rxjang.piece.presentation.piece.converter

import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.presentation.piece.dto.request.CreatePieceRequest

object PieceConverter {

    fun CreatePieceRequest.toCommand(): CreatePieceCommand {
        return CreatePieceCommand(
            teacherId = TeacherId(this.teacherId),
            title = this.title,
            problemIds = this.problemIds.map { ProblemId(it) }.toSet(),
        )
    }
}