package com.example.projectakhir.domain.usecase

import com.example.projectakhir.domain.model.StatisticsSummary
import com.example.projectakhir.domain.model.StudySession
import com.example.projectakhir.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class SaveSessionUseCase(private val repository: SessionRepository) {
    suspend operator fun invoke(session: StudySession): Long {
        return repository.saveSession(session)
    }
}

class GetHistoryUseCase(private val repository: SessionRepository) {
    operator fun invoke(): Flow<List<StudySession>> {
        return repository.getAllSessions()
    }

    suspend fun getById(id: Long): StudySession? {
        return repository.getSessionById(id)
    }
}

class GetStatisticsUseCase(private val repository: SessionRepository) {
    operator fun invoke(): Flow<StatisticsSummary> {
        return repository.getStatistics()
    }
}

class DeleteSessionUseCase(private val repository: SessionRepository) {
    suspend fun deleteById(id: Long) {
        repository.deleteSessionById(id)
    }

    suspend fun deleteAll() {
        repository.deleteAllSessions()
    }
}
