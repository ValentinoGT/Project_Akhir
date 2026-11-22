package com.example.projectakhir.data.repository

import com.example.projectakhir.data.local.SessionDao
import com.example.projectakhir.data.local.SessionEntity
import com.example.projectakhir.domain.model.DailyBreakdown
import com.example.projectakhir.domain.model.SessionStatus
import com.example.projectakhir.domain.model.StatisticsSummary
import com.example.projectakhir.domain.model.StudySession
import com.example.projectakhir.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun saveSession(session: StudySession): Long {
        return sessionDao.insertSession(session.toEntity())
    }

    override fun getAllSessions(): Flow<List<StudySession>> {
        return sessionDao.getAllSessions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSessionById(id: Long): StudySession? {
        return sessionDao.getSessionById(id)?.toDomain()
    }

    override suspend fun deleteSessionById(id: Long) {
        sessionDao.deleteSessionById(id)
    }

    override suspend fun deleteAllSessions() {
        sessionDao.deleteAllSessions()
    }

    override fun getStatistics(): Flow<StatisticsSummary> {
        val startOfToday = getStartOfTodayMillis()
        val startOfSevenDaysAgo = getStartOfDayMillis(6)

        return sessionDao.getSessionsSince(startOfSevenDaysAgo).map { sessionsSince ->
            val completedSessions = sessionsSince.filter { it.status == "COMPLETED" }

            val todaySessions = completedSessions.filter { it.startedAt >= startOfToday }
            val todayTotalSec = todaySessions.sumOf { it.actualDurationSec.toLong() }
            val todayCount = todaySessions.size

            val sevenDaysTotalSec = completedSessions.sumOf { it.actualDurationSec.toLong() }
            val sevenDaysCount = completedSessions.size

            val avgDurationSec = if (sevenDaysCount > 0) sevenDaysTotalSec / sevenDaysCount else 0L

            val dayFormatter = SimpleDateFormat("EEE", Locale.getDefault())
            val dailyBreakdowns = ArrayList<DailyBreakdown>()

            for (i in 6 downTo 0) {
                val dayStart = getStartOfDayMillis(i)
                val dayEnd = dayStart + 86_400_000L - 1L

                val daySessions = completedSessions.filter { it.startedAt in dayStart..dayEnd }
                val dayTotalSec = daySessions.sumOf { it.actualDurationSec.toLong() }
                val dayCount = daySessions.size

                val cal = Calendar.getInstance().apply { timeInMillis = dayStart }
                val dayName = dayFormatter.format(cal.time)

                dailyBreakdowns.add(
                    DailyBreakdown(
                        dayName = dayName,
                        dateMillis = dayStart,
                        totalSeconds = dayTotalSec,
                        sessionCount = dayCount
                    )
                )
            }

            StatisticsSummary(
                todayTotalSeconds = todayTotalSec,
                todaySessionCount = todayCount,
                sevenDaysTotalSeconds = sevenDaysTotalSec,
                sevenDaysSessionCount = sevenDaysCount,
                averageDurationSeconds = avgDurationSec,
                dailyBreakdown = dailyBreakdowns
            )
        }
    }

    private fun getStartOfTodayMillis(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun getStartOfDayMillis(daysAgo: Int): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -daysAgo)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}

fun SessionEntity.toDomain(): StudySession {
    return StudySession(
        id = id,
        startedAt = startedAt,
        endedAt = endedAt,
        plannedDurationSec = plannedDurationSec,
        actualDurationSec = actualDurationSec,
        avgLux = avgLux,
        minLux = minLux,
        maxLux = maxLux,
        movementEvents = movementEvents,
        movementDurationSec = movementDurationSec,
        status = try {
            SessionStatus.valueOf(status)
        } catch (_: Exception) {
            SessionStatus.COMPLETED
        },
        note = note
    )
}

fun StudySession.toEntity(): SessionEntity {
    return SessionEntity(
        id = id,
        startedAt = startedAt,
        endedAt = endedAt,
        plannedDurationSec = plannedDurationSec,
        actualDurationSec = actualDurationSec,
        avgLux = avgLux,
        minLux = minLux,
        maxLux = maxLux,
        movementEvents = movementEvents,
        movementDurationSec = movementDurationSec,
        status = status.name,
        note = note
    )
}
