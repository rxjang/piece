package com.rxjang.piece.application.exception

import com.rxjang.piece.application.exception.codes.FailureCode

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