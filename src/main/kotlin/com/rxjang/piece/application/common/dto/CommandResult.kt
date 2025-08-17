package com.rxjang.piece.application.common.dto

sealed interface CommandResult

interface CommandSuccess: CommandResult

interface CommandFailure: CommandResult {
    val failureCode: FailureCode
}