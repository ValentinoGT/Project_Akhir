package com.example.projectakhir.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "started_at")
    val startedAt: Long,

    @ColumnInfo(name = "ended_at")
    val endedAt: Long?,

    @ColumnInfo(name = "planned_duration_sec")
    val plannedDurationSec: Int,

    @ColumnInfo(name = "actual_duration_sec")
    val actualDurationSec: Int,

    @ColumnInfo(name = "avg_lux")
    val avgLux: Float?,

    @ColumnInfo(name = "min_lux")
    val minLux: Float?,

    @ColumnInfo(name = "max_lux")
    val maxLux: Float?,

    @ColumnInfo(name = "movement_events")
    val movementEvents: Int,

    @ColumnInfo(name = "movement_duration_sec")
    val movementDurationSec: Int,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "note")
    val note: String?
)
