package com.rxjang.piece.presentation.exception

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class RestExceptionHandler(
    private val mapper: ObjectMapper,
) {

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(e: Exception): ResponseEntity<RestExceptionResponse> {
        logger.error { e }
        val httpStatus = getHttpStatus(e)
        return ResponseEntity(getRestResponse(httpStatus, e), httpStatus)
    }

    private fun getHttpStatus(e: Exception): HttpStatus {
        if (e is IllegalArgumentException || e is MethodArgumentNotValidException || e is ValidationException) {
            return HttpStatus.BAD_REQUEST
        }
        return HttpStatus.INTERNAL_SERVER_ERROR
    }

    private fun getRestResponse(httpStatus: HttpStatus, e: Exception): RestExceptionResponse {
        return RestExceptionResponse(httpStatus.value().toString(), e.message, null)
    }

}