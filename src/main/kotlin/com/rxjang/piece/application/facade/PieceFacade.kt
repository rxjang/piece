package com.rxjang.piece.application.facade

import com.rxjang.piece.application.common.UserFailureCode
import com.rxjang.piece.application.dto.AssignPieceResult
import com.rxjang.piece.application.service.PieceService
import com.rxjang.piece.application.service.UserService
import com.rxjang.piece.domain.piece.command.AssignPieceCommand
import org.springframework.stereotype.Component

@Component
class PieceFacade(
    private val pieceService: PieceService,
    private val userService: UserService,
) {

    fun assignPieceToStudent(command: AssignPieceCommand): AssignPieceResult {
        // 요청 받은 모든 학생이 존재 하는지 확인
        val students = userService.findStudentById(command.studentIds)
        if (command.studentIds.size != students.size) {
            return AssignPieceResult.Failure(UserFailureCode.SOME_USER_NOT_FOUND)
        }
        return pieceService.assignPieceToStudent(command)
    }
}