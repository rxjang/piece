package com.rxjang.piece.domain.problem.model

enum class ProblemLevel(
    val valueRange: IntRange,
) {
    LOW(1..1),
    MEDIUM(2..4),
    HIGH(5..5),
    ;

    fun getLevelRatio(): LevelRatio {
        return when (this) {
            HIGH -> LevelRatio(0.2, 0.3, 0.5)
            MEDIUM -> LevelRatio(0.25, 0.5, 0.25)
            LOW -> LevelRatio(0.5, 0.3, 0.2)
        }
    }
}

data class LevelRatio(
    val low: Double,
    val medium: Double,
    val high: Double,
)
