package com.rxjang.piece.presentation.problem

import com.rxjang.piece.application.ProblemService
import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.presentation.problem.dto.request.ProblemRequestType
import com.rxjang.piece.presentation.problem.mapper.ProblemConverter.toResponse
import com.rxjang.piece.presentation.problem.dto.response.SearchProblemResponse
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/problems")
@Validated
class ProblemController(
    private val problemService: ProblemService,
) {

    val logger = LoggerFactory.getLogger("ProblemController")

    @GetMapping
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
            logger.error("Error while searching problem", e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "문제 검색 중 오류가 발생했습니다")
        }

    }
}