package com.example.projectakhir.domain.model

data class DailyBreakdown(
    val dayName: String,
    val dateMillis: Long,
    val totalSeconds: Long,
    val sessionCount: Int
)

data class StatisticsSummary(
    val todayTotalSeconds: Long,
    val todaySessionCount: Int,
    val sevenDaysTotalSeconds: Long,
    val sevenDaysSessionCount: Int,
    val averageDurationSeconds: Long,
    val dailyBreakdown: List<DailyBreakdown>
)
