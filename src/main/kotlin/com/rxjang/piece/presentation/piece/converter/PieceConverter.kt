package com.rxjang.piece.presentation.piece.converter

import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import com.rxjang.piece.domain.piece.command.ChangeProblemOrderCommand
import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.command.ProblemWithAnswer
import com.rxjang.piece.domain.piece.command.ScorePieceCommand
import com.rxjang.piece.domain.piece.command.ProblemOrder as DomainProblemOrder
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.domain.user.model.StudentId
import com.rxjang.piece.domain.user.model.TeacherId
import com.rxjang.piece.presentation.piece.dto.request.AssignPieceToStudentRequest
import com.rxjang.piece.presentation.piece.dto.request.ChangeProblemOrderInPieceRequest
import com.rxjang.piece.presentation.piece.dto.request.CreatePieceRequest
import com.rxjang.piece.presentation.piece.dto.request.ProblemOrder
import com.rxjang.piece.presentation.piece.dto.request.ScorePieceRequest
import com.rxjang.piece.presentation.piece.dto.response.ChangeProblemOrderInPieceResponse

object PieceConverter {

    fun CreatePieceRequest.toCommand(): CreatePieceCommand {
        return CreatePieceCommand(
            teacherId = TeacherId(this.teacherId),
            title = this.title,
            problemIds = this.problemIds.map { ProblemId(it) }.toSet(),
        )
    }

    fun ChangeProblemOrderInPieceRequest.toCommand(pieceId: Int): ChangeProblemOrderCommand {
        return ChangeProblemOrderCommand(
            pieceId = PieceId(pieceId),
            teacherId = TeacherId(this.teacherId),
            problemOrders = this.problemOrders.map {
                DomainProblemOrder(
                    problemId = ProblemId(it.problemId),
                    order = it.order,
                )
            }
        )
    }

    fun Piece.toChangeOrderResponse(): ChangeProblemOrderInPieceResponse {
        return ChangeProblemOrderInPieceResponse(
            pieceId = this.id.value,
            problemOrders = this.problemIds.mapIndexed { index, id ->
                ProblemOrder(
                    problemId = id.value,
                    order = index + 1,
                )
            }
        )
    }

    fun AssignPieceToStudentRequest.toCommand(pieceId: Int): AssignPieceCommand {
        return AssignPieceCommand(
            teacherId = TeacherId(this.teacherId),
            pieceId = PieceId(pieceId),
            studentIds = this.studentIds.map { StudentId(it) },
        )
    }

    fun ScorePieceRequest.toCommand(pieceId: Int): ScorePieceCommand {
        return ScorePieceCommand(
            pieceId = PieceId(pieceId),
            studentId = StudentId(this.studentId),
            answers = this.answers.map {
                ProblemWithAnswer(
                    problemId = ProblemId(it.problemId),
                    answer = it.answer,
                )
            }
        )
    }
}