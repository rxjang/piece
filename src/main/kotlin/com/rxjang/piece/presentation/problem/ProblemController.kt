package com.rxjang.piece.presentation.problem

import com.rxjang.piece.application.service.ProblemService
import com.rxjang.piece.domain.problem.query.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.presentation.security.SecurityConstants.TEACHER_ROLE
import com.rxjang.piece.application.dto.request.ProblemRequestType
import com.rxjang.piece.application.dto.converter.ProblemConverter.toResponse
import com.rxjang.piece.application.dto.response.SearchProblemResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/problems")
@Validated
class ProblemController(
    private val problemService: ProblemService,
) {

    @GetMapping
    @PreAuthorize(TEACHER_ROLE)
    fun search(
        @RequestParam
        @Min(value = 1, message = "요청 문제수는 1개 이상이어야 합니다")
        totalCount: Int,
        @RequestParam
        @NotEmpty(message = "unitCode는 비어있을 수 없습니다")
        unitCodeList: List<String>,
        @RequestParam level: ProblemLevel,
        @RequestParam problemType: ProblemRequestType,
    ): ResponseEntity<SearchProblemResponse> {

        return try {
            val searchQuery = SearchProblemQuery(
                requestCount = totalCount,
                unitCodes = unitCodeList,
                problemTypes = problemType.toDomainEnum(),
                level = level
            )
            val result = problemService.searchProblems(searchQuery)

            val response = result.map {
                it.toResponse()
            }

            ResponseEntity.ok(SearchProblemResponse(response))
        } catch (e: Exception) {
            logger.error(e)  {"Error while searching problem" }
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "문제 검색 중 오류가 발생했습니다")
        }

    }
}