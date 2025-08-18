package com.rxjang.piece.application.common

enum class PieceFailureCode(
    override val code: String,
    override val message: String
) : FailureCode {
    SOME_PROBLEMS_NOT_EXIST("001", "존재하지 않는 문제 id가 있습니다."),
    PIECE_NOT_FOUND("002", "학습지를 찾을 수 없습니다."),
    PROBLEM_ID_NOT_MATCHED_FOR_ORDER("003", "학습지에 등록된 문제 id와 순서 수정 요청한 문제 id가 일치하지 않습니다."),
    INVALID_PROBLEM_ORDER("004", "문제 순서를 확인해 주세요"),
    ASSIGNMENT_NOT_FOUND("005", "해당 학습지 출제를 찾을 수 없습니다."),
    ALREADY_COMPLETED("006", "이미 완료된 학습지입니다."),
    ANSWER_MISMATCH("007", "제출된 답변과 문제가 일치하지 않습니다."),
    UNAUTHORIZED_ACCESS("008", "해당 학습지에 대한 접근 권한이 없습니다."),
    ;

    override val prefix: String = "PIECE"
}