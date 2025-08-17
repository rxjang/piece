package com.rxjang.piece.application.piece

import com.rxjang.piece.application.common.dto.FailureCode

enum class PieceFailureCode(
    override val code: String,
    override val message: String
) : FailureCode {
    SOME_PROBLEMS_NOT_EXIST("001", "존재하지 않는 문제 id가 있습니다."),
    INVALID_TEACHER("002", "유효하지 않은 교사입니다."),
    DUPLICATE_PIECE_NAME("003", "이미 존재하는 문제집 이름입니다.");

    override val prefix: String = "PIECE"
}