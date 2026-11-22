package com.example.projectakhir.di

import android.content.Context
import com.example.projectakhir.data.local.FocusSenseDatabase
import com.example.projectakhir.data.repository.SessionRepositoryImpl
import com.example.projectakhir.data.settings.UserPreferencesRepository
import com.example.projectakhir.domain.repository.SessionRepository
import com.example.projectakhir.domain.usecase.DeleteSessionUseCase
import com.example.projectakhir.domain.usecase.GetHistoryUseCase
import com.example.projectakhir.domain.usecase.GetStatisticsUseCase
import com.example.projectakhir.domain.usecase.SaveSessionUseCase
import com.example.projectakhir.sensor.LightSensorMonitor
import com.example.projectakhir.sensor.MotionSensorMonitor

class AppContainer(private val context: Context) {
    val database: FocusSenseDatabase by lazy { FocusSenseDatabase.getDatabase(context) }
    val sessionRepository: SessionRepository by lazy { SessionRepositoryImpl(database.sessionDao()) }
    val userPreferencesRepository: UserPreferencesRepository by lazy { UserPreferencesRepository(context) }
    val lightSensorMonitor: LightSensorMonitor by lazy { LightSensorMonitor(context) }
    val motionSensorMonitor: MotionSensorMonitor by lazy { MotionSensorMonitor(context) }
    val saveSessionUseCase: SaveSessionUseCase by lazy { SaveSessionUseCase(sessionRepository) }
    val getHistoryUseCase: GetHistoryUseCase by lazy { GetHistoryUseCase(sessionRepository) }
    val getStatisticsUseCase: GetStatisticsUseCase by lazy { GetStatisticsUseCase(sessionRepository) }
    val deleteSessionUseCase: DeleteSessionUseCase by lazy { DeleteSessionUseCase(sessionRepository) }
}
