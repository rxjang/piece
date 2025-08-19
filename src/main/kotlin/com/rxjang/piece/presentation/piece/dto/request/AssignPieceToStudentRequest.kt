package com.rxjang.piece.presentation.piece.dto.request

import jakarta.validation.constraints.NotEmpty
import org.springframework.validation.annotation.Validated

@Validated
data class AssignPieceToStudentRequest(
    @field:NotEmpty(message = "최소 1명의 학생을 선택해주세요.")
    val studentIds: List<Int>,
)
