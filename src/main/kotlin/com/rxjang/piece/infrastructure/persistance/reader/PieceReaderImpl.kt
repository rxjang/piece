package com.rxjang.piece.infrastructure.persistance.reader

import com.rxjang.piece.domain.piece.model.Piece
import com.rxjang.piece.domain.piece.model.PieceId
import com.rxjang.piece.domain.piece.reader.PieceReader
import com.rxjang.piece.infrastructure.persistance.mapper.PieceMapper.toModel
import com.rxjang.piece.infrastructure.persistance.repository.PieceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PieceReaderImpl(
    private val pieceRepository: PieceRepository,
): PieceReader {

    override fun findById(id: PieceId): Piece? {
        return pieceRepository.findByIdWithProblems(id.value)?.toModel(true)
    }


}