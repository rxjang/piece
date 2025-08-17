package com.rxjang.piece.application.piece

import com.rxjang.piece.application.common.dto.FailureCode

enum class PieceFailureCode(
    override val code: String,
    override val message: String
) : FailureCode {
    SOME_PROBLEMS_NOT_EXIST("001", "존재하지 않는 문제 id가 있습니다."),
    PIECE_NOT_FOUND("002", "학습지를 찾을 수 없습니다."),
    PROBLEM_ID_NOT_MATCHED_FOR_ORDER("003", "학습지에 등록된 문제 id와 순서 수정 요청한 문제 id가 일치하지 않습니다."),
    INVALID_PROBLEM_ORDER("004", "문제 순서를 확인해 주세요")
    ;

    override val prefix: String = "PIECE"
}