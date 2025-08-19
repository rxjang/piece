package com.rxjang.piece.presentation.piece.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.validation.annotation.Validated

@Validated
data class CreatePieceRequest(
    @field:Size(min = 2, max = 100, message = "제목은 2자 이상 100자 이하로 입력해주세요.")
    @field:NotBlank(message = "제목을 입력해주세요.")
    val title: String,
    @field:Size(min = 1, max = 50, message = "문제는 1개에서 50개 사이로 등록해주세요.")
    @field:NotEmpty(message = "최소 1개의 문제를 선택해주세요.")
    val problemIds: List<Int>
)
