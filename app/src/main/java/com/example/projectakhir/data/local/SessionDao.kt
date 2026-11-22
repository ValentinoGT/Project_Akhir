package com.example.projectakhir.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long

    @Query("SELECT * FROM study_sessions ORDER BY started_at DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM study_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): SessionEntity?

    @Query("DELETE FROM study_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)

    @Query("DELETE FROM study_sessions")
    suspend fun deleteAllSessions()

    @Query("SELECT SUM(actual_duration_sec) FROM study_sessions WHERE started_at >= :startTime")
    fun getTotalDurationSince(startTime: Long): Flow<Long?>

    @Query("SELECT COUNT(*) FROM study_sessions WHERE started_at >= :startTime AND status = 'COMPLETED'")
    fun getCompletedCountSince(startTime: Long): Flow<Int>

    @Query("SELECT * FROM study_sessions WHERE started_at >= :startTime ORDER BY started_at DESC")
    fun getSessionsSince(startTime: Long): Flow<List<SessionEntity>>
}
