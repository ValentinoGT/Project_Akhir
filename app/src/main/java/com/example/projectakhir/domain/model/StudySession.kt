package com.example.projectakhir.domain.model

data class StudySession(
    val id: Long = 0,
    val startedAt: Long,
    val endedAt: Long?,
    val plannedDurationSec: Int,
    val actualDurationSec: Int,
    val avgLux: Float?,
    val minLux: Float?,
    val maxLux: Float?,
    val movementEvents: Int,
    val movementDurationSec: Int,
    val status: SessionStatus,
    val note: String?
)

enum class SessionStatus {
    COMPLETED,
    ABORTED
}
