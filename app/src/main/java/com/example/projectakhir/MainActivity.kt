package com.example.projectakhir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.projectakhir.sensor.TimerState
import com.example.projectakhir.ui.components.BottomNavigationBar
import com.example.projectakhir.ui.components.ScreenTab
import com.example.projectakhir.ui.screens.FocusModeScreen
import com.example.projectakhir.ui.screens.HistoryScreen
import com.example.projectakhir.ui.screens.HomeScreen
import com.example.projectakhir.ui.screens.InsightsScreen
import com.example.projectakhir.ui.screens.SensorDetailsScreen
import com.example.projectakhir.ui.screens.SessionCompleteScreen
import com.example.projectakhir.ui.screens.SessionDetailsScreen
import com.example.projectakhir.ui.screens.SettingsScreen
import com.example.projectakhir.ui.theme.FocusSenseTheme
import com.example.projectakhir.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        val appContainer = (application as FocusSenseApplication).container
        MainViewModel.provideFactory(
            lightSensorMonitor = appContainer.lightSensorMonitor,
            motionSensorMonitor = appContainer.motionSensorMonitor,
            saveSessionUseCase = appContainer.saveSessionUseCase,
            getHistoryUseCase = appContainer.getHistoryUseCase,
            getStatisticsUseCase = appContainer.getStatisticsUseCase,
            deleteSessionUseCase = appContainer.deleteSessionUseCase,
            userPreferencesRepository = appContainer.userPreferencesRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            FocusSenseTheme(themeMode = uiState.themeMode) {
                MainAppContent(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }
        }
    }
}

@Composable
private fun MainAppContent(
    viewModel: MainViewModel,
    uiState: com.example.projectakhir.ui.viewmodel.MainUiState
) {
    var selectedTab by remember { mutableStateOf(ScreenTab.HOME) }
    var isViewingSensorDetails by remember { mutableStateOf(false) }

    val timerState = uiState.timerStatus.state
    val isFocusing = timerState == TimerState.RUNNING || timerState == TimerState.PAUSED
    val isSessionComplete = uiState.lastCompletedSession != null && !isFocusing

    if (isFocusing) {
        FocusModeScreen(
            uiState = uiState,
            onPauseClick = { viewModel.pauseTimer() },
            onResumeClick = { viewModel.resumeTimer() },
            onEndClick = { viewModel.abortTimer() }
        )
    } else if (isSessionComplete) {
        SessionCompleteScreen(
            uiState = uiState,
            onSaveClick = { note ->
                viewModel.saveNoteAndDismiss(note)
            }
        )
    } else if (isViewingSensorDetails) {
        SensorDetailsScreen(
            uiState = uiState,
            onBackClick = { isViewingSensorDetails = false }
        )
    } else if (uiState.selectedSession != null) {
        SessionDetailsScreen(
            session = uiState.selectedSession,
            onBackClick = { viewModel.selectSessionForDetail(null) },
            onDeleteClick = { id -> viewModel.deleteSession(id) }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = selectedTab.route,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            val screenModifier = Modifier.padding(innerPadding)
            when (selectedTab) {
                ScreenTab.HOME -> HomeScreen(
                    uiState = uiState,
                    onStartFocusClick = { viewModel.startTimer() },
                    onPauseClick = { viewModel.pauseTimer() },
                    onResumeClick = { viewModel.resumeTimer() },
                    onEndClick = { viewModel.abortTimer() },
                    onDurationSelected = { sec -> viewModel.setSelectedDuration(sec) },
                    onSensorDetailsClick = { isViewingSensorDetails = true },
                    modifier = screenModifier
                )

                ScreenTab.HISTORY -> HistoryScreen(
                    uiState = uiState,
                    onSessionClick = { session -> viewModel.selectSessionForDetail(session) },
                    modifier = screenModifier
                )

                ScreenTab.INSIGHTS -> InsightsScreen(
                    uiState = uiState,
                    modifier = screenModifier
                )

                ScreenTab.SETTINGS -> SettingsScreen(
                    uiState = uiState,
                    onThemeModeChange = { mode -> viewModel.setThemeMode(mode) },
                    onNotificationChange = { enabled -> viewModel.setNotificationEnabled(enabled) },
                    onVibrationChange = { enabled -> viewModel.setVibrationEnabled(enabled) },
                    onDeleteAllHistoryClick = { viewModel.deleteAllSessions() },
                    modifier = screenModifier
                )
            }
        }
    }
}
