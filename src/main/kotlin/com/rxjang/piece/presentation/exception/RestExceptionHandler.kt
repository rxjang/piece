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
        logger.error(e) { "예외 발생: ${e.message}" }
        val httpStatus = getHttpStatus(e)
        return ResponseEntity(getRestResponse(httpStatus, e), httpStatus)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<RestExceptionResponse> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage ?: "유효하지 않은 값입니다."
        }
        return ResponseEntity.badRequest().body(
            RestExceptionResponse("NOT_VALID", "요청 값을 확인해 주세요.", errors),
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessExceptions(
        ex: BusinessException
    ): ResponseEntity<RestExceptionResponse> {
        return ResponseEntity.badRequest().body(
            RestExceptionResponse(ex.errorCode, ex.message),
        )
    }

    private fun getHttpStatus(e: Exception): HttpStatus {
        if (e is IllegalArgumentException || e is MethodArgumentNotValidException || e is ValidationException) {
            return HttpStatus.BAD_REQUEST
        }
        return HttpStatus.INTERNAL_SERVER_ERROR
    }

    private fun getRestResponse(httpStatus: HttpStatus, e: Exception): RestExceptionResponse {
        return RestExceptionResponse("UNKNOWN", e.message)
    }

}