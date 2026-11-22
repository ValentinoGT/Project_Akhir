package com.example.projectakhir.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectakhir.sensor.TimerState
import com.example.projectakhir.ui.components.AppHeader
import com.example.projectakhir.ui.components.DailyProgressCard
import com.example.projectakhir.ui.components.DurationSelector
import com.example.projectakhir.ui.components.FocusTimerCard
import com.example.projectakhir.ui.components.SensorStatusCard
import com.example.projectakhir.ui.viewmodel.MainUiState

@Composable
fun HomeScreen(
    uiState: MainUiState,
    onStartFocusClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onEndClick: () -> Unit,
    onDurationSelected: (Long) -> Unit,
    onSensorDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isRunning = uiState.timerStatus.state == TimerState.RUNNING
    val isPaused = uiState.timerStatus.state == TimerState.PAUSED
    val isIdle = uiState.timerStatus.state == TimerState.IDLE

    val displayRemainingSeconds = if (isIdle) uiState.selectedDurationSec else uiState.timerStatus.remainingSeconds
    val displayTargetSeconds = if (isIdle) uiState.selectedDurationSec else uiState.timerStatus.targetSeconds

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AppHeader(
            title = "FocusSense",
            subtitle = "Kelola fokus dan optimalkan lingkungan belajar"
        )

        Spacer(modifier = Modifier.height(8.dp))

        DailyProgressCard(
            todayTotalSeconds = uiState.statistics?.todayTotalSeconds ?: 0L,
            completedCount = uiState.statistics?.todaySessionCount ?: 0
        )

        Spacer(modifier = Modifier.height(16.dp))

        SensorStatusCard(
            currentLux = uiState.lightData.currentLux,
            lightState = uiState.lightData.lightState,
            isLightAvailable = uiState.lightData.isAvailable,
            currentMagnitude = uiState.motionData.currentMagnitude,
            movementState = uiState.motionData.movementState,
            isMotionAvailable = uiState.motionData.isAvailable,
            onDetailsClick = onSensorDetailsClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isIdle) {
            DurationSelector(
                selectedDurationSec = uiState.selectedDurationSec,
                onDurationSelected = onDurationSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        FocusTimerCard(
            remainingSeconds = displayRemainingSeconds,
            targetSeconds = displayTargetSeconds,
            isRunning = isRunning,
            isPaused = isPaused,
            isIdle = isIdle,
            onStartClick = onStartFocusClick,
            onPauseClick = onPauseClick,
            onResumeClick = onResumeClick,
            onEndClick = onEndClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
