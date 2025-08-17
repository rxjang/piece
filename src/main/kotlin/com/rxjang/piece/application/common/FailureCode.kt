package com.rxjang.piece.application.common

interface FailureCode {
    val code: String
    val message: String
    val prefix: String

    fun fullCode(): String = "${prefix}_${code}"
}

