package com.rxjang.piece.application.facade

import com.rxjang.piece.application.exception.codes.UserFailureCode
import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.dto.converter.PieceConverter.toCommand
import com.rxjang.piece.application.dto.request.AssignPieceToStudentRequest
import com.rxjang.piece.application.service.PieceService
import com.rxjang.piece.application.service.UserService
import org.springframework.stereotype.Component

@Component
class PieceFacade(
    private val pieceService: PieceService,
    private val userService: UserService,
) {

    fun assignPieceToStudent(pieceId: Int, request: AssignPieceToStudentRequest): AssignPieceResult {
        val command = request.toCommand(pieceId)
        // 요청 받은 모든 학생이 존재 하는지 확인
        val students = userService.findStudentById(command.studentIds)
        if (command.studentIds.size != students.size) {
            return AssignPieceResult.Failure(UserFailureCode.SOME_USER_NOT_FOUND)
        }
        return pieceService.assignPieceToStudent(command)
    }
}