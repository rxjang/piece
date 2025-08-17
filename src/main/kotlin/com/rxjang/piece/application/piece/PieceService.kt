package com.rxjang.piece.application.piece

import com.rxjang.piece.application.piece.dto.CreatePieceFailure
import com.rxjang.piece.application.piece.dto.CreatePieceResult
import com.rxjang.piece.application.piece.dto.CreatePieceSuccess
import com.rxjang.piece.domain.piece.command.CreatePieceCommand
import com.rxjang.piece.domain.piece.store.PieceStore
import com.rxjang.piece.domain.problem.reader.ProblemReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PieceService(
    private val problemReader: ProblemReader,
    private val pieceStore: PieceStore,
) {

    @Transactional
    fun createPiece(command: CreatePieceCommand): CreatePieceResult {
        // 문제 정렬해서 가져오기
        val problems = problemReader.getOrderedProblemsByIds(command.problemIds.toList())
        if (problems.size != command.problemIds.size) {
            return CreatePieceFailure(PieceFailureCode.SOME_PROBLEMS_NOT_EXIST)
        }
        // 정렬된 순서로 학습지 생성
        val piece = pieceStore.createPiece(command.copy(problemIds = problems.map { it.id }.toSet()))
        return CreatePieceSuccess(piece)
    }
}