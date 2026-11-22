package com.example.projectakhir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectakhir.data.settings.UserPreferencesRepository
import com.example.projectakhir.domain.model.SessionStatus
import com.example.projectakhir.domain.model.StatisticsSummary
import com.example.projectakhir.domain.model.StudySession
import com.example.projectakhir.domain.usecase.DeleteSessionUseCase
import com.example.projectakhir.domain.usecase.GetHistoryUseCase
import com.example.projectakhir.domain.usecase.GetStatisticsUseCase
import com.example.projectakhir.domain.usecase.SaveSessionUseCase
import com.example.projectakhir.sensor.LightData
import com.example.projectakhir.sensor.LightSensorMonitor
import com.example.projectakhir.sensor.MotionData
import com.example.projectakhir.sensor.MotionSensorMonitor
import com.example.projectakhir.sensor.TimerEngine
import com.example.projectakhir.sensor.TimerState
import com.example.projectakhir.sensor.TimerStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val selectedDurationSec: Long = 1500L,
    val currentRoute: String = "home",
    val timerStatus: TimerStatus = TimerStatus(),
    val lightData: LightData = LightData(),
    val motionData: MotionData = MotionData(),
    val sessions: List<StudySession> = emptyList(),
    val statistics: StatisticsSummary? = null,
    val selectedSession: StudySession? = null,
    val lastCompletedSession: StudySession? = null,
    val themeMode: String = "SYSTEM",
    val notificationEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true
)

class MainViewModel(
    private val timerEngine: TimerEngine = TimerEngine(),
    private val lightSensorMonitor: LightSensorMonitor,
    private val motionSensorMonitor: MotionSensorMonitor,
    private val saveSessionUseCase: SaveSessionUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getStatisticsUseCase: GetStatisticsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var timerLoopJob: Job? = null
    private var sessionStartTimestamp: Long = 0L

    init {
        observeData()

        // Start ambient real-time sensors immediately on app launch
        lightSensorMonitor.startListening()
        motionSensorMonitor.startListening()

        timerEngine.onTimerTick = { status ->
            _uiState.update { it.copy(timerStatus = status) }
        }

        timerEngine.onTargetReached = {
            finishTimer()
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            userPreferencesRepository.themeMode.collectLatest { mode ->
                _uiState.update { it.copy(themeMode = mode) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.defaultTargetDurationSec.collectLatest { duration ->
                if (timerEngine.state == TimerState.IDLE) {
                    _uiState.update {
                        it.copy(
                            selectedDurationSec = duration,
                            timerStatus = it.timerStatus.copy(
                                targetSeconds = duration,
                                remainingSeconds = duration
                            )
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.notificationEnabled.collectLatest { enabled ->
                _uiState.update { it.copy(notificationEnabled = enabled) }
            }
        }

        viewModelScope.launch {
            userPreferencesRepository.vibrationEnabled.collectLatest { enabled ->
                _uiState.update { it.copy(vibrationEnabled = enabled) }
            }
        }

        viewModelScope.launch {
            lightSensorMonitor.lightData.collectLatest { data ->
                _uiState.update { it.copy(lightData = data) }
            }
        }

        viewModelScope.launch {
            motionSensorMonitor.motionData.collectLatest { data ->
                _uiState.update { it.copy(motionData = data) }
            }
        }

        viewModelScope.launch {
            getHistoryUseCase().collectLatest { sessionList ->
                _uiState.update { it.copy(sessions = sessionList) }
            }
        }

        viewModelScope.launch {
            getStatisticsUseCase().collectLatest { stats ->
                _uiState.update { it.copy(statistics = stats) }
            }
        }
    }

    fun setSelectedDuration(seconds: Long) {
        if (timerEngine.state == TimerState.IDLE) {
            userPreferencesRepository.setDefaultTargetDurationSec(seconds)
            _uiState.update {
                it.copy(
                    selectedDurationSec = seconds,
                    timerStatus = it.timerStatus.copy(
                        targetSeconds = seconds,
                        remainingSeconds = seconds
                    )
                )
            }
        }
    }

    fun startTimer() {
        sessionStartTimestamp = System.currentTimeMillis()
        lightSensorMonitor.startListening()
        motionSensorMonitor.startListening()

        timerEngine.start(_uiState.value.selectedDurationSec)
        startTimerLoop()
    }

    fun pauseTimer() {
        timerEngine.pause()
    }

    fun resumeTimer() {
        timerEngine.resume()
    }

    fun finishTimer() {
        val actualDurationSec = timerEngine.finish().toInt()
        stopTimerLoop()

        val lightData = _uiState.value.lightData
        val motionData = _uiState.value.motionData

        val newSession = StudySession(
            startedAt = sessionStartTimestamp,
            endedAt = System.currentTimeMillis(),
            plannedDurationSec = _uiState.value.selectedDurationSec.toInt(),
            actualDurationSec = actualDurationSec,
            avgLux = lightData.avgLux,
            minLux = lightData.minLux,
            maxLux = lightData.maxLux,
            movementEvents = motionData.movementEventsCount,
            movementDurationSec = motionData.movementDurationSec,
            status = SessionStatus.COMPLETED,
            note = null
        )

        viewModelScope.launch {
            val insertedId = saveSessionUseCase(newSession)
            _uiState.update { it.copy(lastCompletedSession = newSession.copy(id = insertedId)) }
        }
    }

    fun abortTimer() {
        val actualDurationSec = timerEngine.abort().toInt()
        stopTimerLoop()

        val lightData = _uiState.value.lightData
        val motionData = _uiState.value.motionData

        val newSession = StudySession(
            startedAt = sessionStartTimestamp,
            endedAt = System.currentTimeMillis(),
            plannedDurationSec = _uiState.value.selectedDurationSec.toInt(),
            actualDurationSec = actualDurationSec,
            avgLux = lightData.avgLux,
            minLux = lightData.minLux,
            maxLux = lightData.maxLux,
            movementEvents = motionData.movementEventsCount,
            movementDurationSec = motionData.movementDurationSec,
            status = SessionStatus.ABORTED,
            note = null
        )

        viewModelScope.launch {
            val insertedId = saveSessionUseCase(newSession)
            _uiState.update { it.copy(lastCompletedSession = newSession.copy(id = insertedId)) }
        }
    }

    fun saveNoteAndDismiss(note: String?) {
        val lastSession = _uiState.value.lastCompletedSession
        if (lastSession != null && !note.isNull_or_empty_safe()) {
            val updatedSession = lastSession.copy(note = note)
            viewModelScope.launch {
                saveSessionUseCase(updatedSession)
                _uiState.update { it.copy(lastCompletedSession = null) }
            }
        } else {
            _uiState.update { it.copy(lastCompletedSession = null) }
        }
        timerEngine.reset()
    }

    fun resetTimer() {
        timerEngine.reset()
        _uiState.update { it.copy(lastCompletedSession = null) }
    }

    private fun startTimerLoop() {
        timerLoopJob?.cancel()
        timerLoopJob = viewModelScope.launch {
            while (timerEngine.state == TimerState.RUNNING || timerEngine.state == TimerState.PAUSED) {
                timerEngine.tick()
                delay(200L)
            }
        }
    }

    private fun stopTimerLoop() {
        timerLoopJob?.cancel()
        timerLoopJob = null
    }

    fun selectSessionForDetail(session: StudySession?) {
        _uiState.update { it.copy(selectedSession = session) }
    }

    fun deleteSession(id: Long) {
        viewModelScope.launch {
            deleteSessionUseCase.deleteById(id)
            if (_uiState.value.selectedSession?.id == id) {
                _uiState.update { it.copy(selectedSession = null) }
            }
        }
    }

    fun deleteAllSessions() {
        viewModelScope.launch {
            deleteSessionUseCase.deleteAll()
            _uiState.update { it.copy(selectedSession = null) }
        }
    }

    fun setThemeMode(mode: String) {
        userPreferencesRepository.setThemeMode(mode)
    }

    fun setNotificationEnabled(enabled: Boolean) {
        userPreferencesRepository.setNotificationEnabled(enabled)
    }

    fun setVibrationEnabled(enabled: Boolean) {
        userPreferencesRepository.setVibrationEnabled(enabled)
    }

    override fun onCleared() {
        super.onCleared()
        lightSensorMonitor.stopListening()
        motionSensorMonitor.stopListening()
    }

    companion object {
        fun provideFactory(
            lightSensorMonitor: LightSensorMonitor,
            motionSensorMonitor: MotionSensorMonitor,
            saveSessionUseCase: SaveSessionUseCase,
            getHistoryUseCase: GetHistoryUseCase,
            getStatisticsUseCase: GetStatisticsUseCase,
            deleteSessionUseCase: DeleteSessionUseCase,
            userPreferencesRepository: UserPreferencesRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(
                    lightSensorMonitor = lightSensorMonitor,
                    motionSensorMonitor = motionSensorMonitor,
                    saveSessionUseCase = saveSessionUseCase,
                    getHistoryUseCase = getHistoryUseCase,
                    getStatisticsUseCase = getStatisticsUseCase,
                    deleteSessionUseCase = deleteSessionUseCase,
                    userPreferencesRepository = userPreferencesRepository
                ) as T
            }
        }
    }
}

private fun String?.isNull_or_empty_safe(): Boolean {
    return this == null || this.trim().isEmpty()
}
