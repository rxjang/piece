package com.rxjang.piece.infrastructure.persistance.store

import com.rxjang.piece.domain.piece.command.ChangeProblemOrderCommand
import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.store.PieceStore
import com.rxjang.piece.domain.problem.model.ProblemId
import com.rxjang.piece.infrastructure.persistance.entity.PieceEntity
import com.rxjang.piece.infrastructure.persistance.entity.PieceProblemEntity
import com.rxjang.piece.infrastructure.persistance.repository.PieceProblemRepository
import com.rxjang.piece.infrastructure.persistance.repository.PieceRepository
import org.springframework.stereotype.Repository

@Repository
class PieceStoreImpl(
    private val pieceRepository: PieceRepository,
    private val pieceProblemRepository: PieceProblemRepository,
): PieceStore {

    override fun createPiece(command: CreatePieceCommand): Piece {
        val piece = PieceEntity(
            title = command.title,
            userId = command.teacherId.value,
        )
        val saved = pieceRepository.save(piece)
        val pieceId = saved.id!!
        val pieceProblem = command.problemIds
            .mapIndexed { index, problemId ->
                PieceProblemEntity(
                    pieceId = pieceId,
                    problemId = problemId.value,
                    order = index + 1,
                )
            }
        pieceProblemRepository.saveAll(pieceProblem)
        return Piece(
            id = PieceId(pieceId),
            title = saved.title,
            userId = command.teacherId,
            problemIds = command.problemIds.toList()
        )
    }

    override fun changeProblemOrder(command: ChangeProblemOrderCommand): List<ProblemId> {
        // 대상 확인
        val pieceProblems = pieceProblemRepository.findByPieceIdAndProblemIdIn(
            pieceId = command.pieceId.value,
            problemIds= command.problemOrders.map { it.problemId.value })
        // 순서 변경
        val changed = command.problemOrders.map {
            val target = pieceProblems.first { pieceProblem -> pieceProblem.problemId == it.problemId.value }
            target.changeOrder(it.order)
        }
        // 저장
        val updated = pieceProblemRepository.saveAll(changed)
        return updated.sortedBy { it.order }.map { ProblemId(it.problemId) }
    }
}