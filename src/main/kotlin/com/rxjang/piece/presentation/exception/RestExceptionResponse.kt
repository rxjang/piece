package com.rxjang.piece.presentation.exception

import org.springframework.http.HttpStatus

data class RestExceptionResponse(
    val errorCode: String,
    val message: String?,
    var detail: Map<String, String> = emptyMap()
)