package com.rxjang.piece.presentation.problem

import com.rxjang.piece.application.ProblemService
import com.rxjang.piece.application.dto.SearchProblemQuery
import com.rxjang.piece.domain.problem.model.ProblemLevel
import com.rxjang.piece.domain.problem.model.ProblemType
import com.rxjang.piece.presentation.problem.mapper.ProblemConverter.toResponse
import com.rxjang.piece.presentation.problem.response.SearchProblemResponse
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
        @RequestParam totalCount: Int,
        @RequestParam unitCodeList: List<String>,
        @RequestParam level: ProblemLevel,
        @RequestParam problemType: ProblemType,
    ): ResponseEntity<SearchProblemResponse> {
        return try {
            validateRequest(totalCount, unitCodeList)

            val searchQuery = SearchProblemQuery(
                requestCount = totalCount,
                unitCodes = unitCodeList,
                problemType = problemType,
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

    private fun validateRequest(
        totalCount: Int,
        unitCodeList: List<String>
    ) {
        // 단원 코드 목록 검증
        if (unitCodeList.isEmpty()) {
            TODO("단원 코드 목록이 비어있습니다")
        }

        if (unitCodeList.size > 50) {
            TODO("단원 코드는 최대 50개까지 요청 가능합니다")
        }

        // 단원 코드 형식 검증
        unitCodeList.forEach { unitCode ->
            if (!unitCode.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                TODO("잘못된 단원 코드 형식: $unitCode")
            }
        }

        if (totalCount <= 0) {
            TODO("총 문제수는 음수 일 수 없습니다")
        }
    }
}