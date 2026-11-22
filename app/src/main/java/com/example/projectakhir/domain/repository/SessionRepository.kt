package com.example.projectakhir.domain.repository

import com.example.projectakhir.domain.model.StatisticsSummary
import com.example.projectakhir.domain.model.StudySession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    suspend fun saveSession(session: StudySession): Long
    fun getAllSessions(): Flow<List<StudySession>>
    suspend fun getSessionById(id: Long): StudySession?
    suspend fun deleteSessionById(id: Long)
    suspend fun deleteAllSessions()
    fun getStatistics(): Flow<StatisticsSummary>
}
