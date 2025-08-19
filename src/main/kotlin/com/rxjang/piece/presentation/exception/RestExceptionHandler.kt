package com.rxjang.piece.presentation.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.rxjang.piece.application.exception.codes.AuthFailureCode
import com.rxjang.piece.application.exception.BusinessException
import com.rxjang.piece.application.exception.codes.UserFailureCode
import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.JwtException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
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

    // JWT 토큰 관련 예외 처리
    @ExceptionHandler(JwtException::class)
    fun handleJwtException(ex: JwtException): ResponseEntity<RestExceptionResponse> {
        val errorResponse = RestExceptionResponse(
            errorCode = AuthFailureCode.INVALID_TOKEN.fullCode(),
            message = AuthFailureCode.INVALID_TOKEN.message,
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    // 인증 실패 예외 처리 (로그인 시)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentialsException(ex: BadCredentialsException): ResponseEntity<RestExceptionResponse> {
        val errorResponse = RestExceptionResponse(
            errorCode = AuthFailureCode.INVALID_CREDENTIALS.fullCode(),
            message = AuthFailureCode.INVALID_CREDENTIALS.message,
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    // 사용자를 찾을 수 없는 경우
    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(ex: UsernameNotFoundException): ResponseEntity<RestExceptionResponse> {
        val errorResponse = RestExceptionResponse(
            errorCode = UserFailureCode.CANNOT_FIND_USER.fullCode(),
            message = UserFailureCode.CANNOT_FIND_USER.message,
        )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
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