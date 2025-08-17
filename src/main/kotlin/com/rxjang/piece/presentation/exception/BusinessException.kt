package com.rxjang.piece.presentation.exception

import com.rxjang.piece.application.common.FailureCode

class BusinessException(
    val errorCode: String,
    val errorMessage: String,
) : RuntimeException(errorMessage) {

    constructor(
        failureCode: FailureCode,
        details: Map<String, Any>? = null
    ): this(
        errorCode = failureCode.fullCode(),
        errorMessage = failureCode.message,
    )
}