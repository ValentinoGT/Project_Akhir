package com.example.projectakhir.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.sensor.TimerState
import com.example.projectakhir.ui.components.formatTime
import com.example.projectakhir.ui.viewmodel.MainUiState

@Composable
fun FocusModeScreen(
    uiState: MainUiState,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning = uiState.timerStatus.state == TimerState.RUNNING
    val isPaused = uiState.timerStatus.state == TimerState.PAUSED

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "MODE FOKUS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isPaused) "Sesi Sedang Dipause" else "Tetap Fokus pada Tugas Anda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Big Central Timer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(uiState.timerStatus.remainingSeconds),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Sensor Status Chip Summary
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (uiState.lightData.isAvailable && uiState.lightData.currentLux != null)
                                "Cahaya: ${uiState.lightData.currentLux.toInt()} Lux"
                            else "Cahaya: N/A",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (uiState.motionData.isAvailable)
                                "Gerakan: ${uiState.motionData.movementState.label}"
                            else "Gerakan: N/A",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Control Buttons
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRunning) {
                    Button(
                        onClick = onPauseClick,
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = "Pause", modifier = Modifier.size(32.dp))
                    }
                } else if (isPaused) {
                    Button(
                        onClick = onResumeClick,
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Lanjutkan", modifier = Modifier.size(32.dp))
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                OutlinedButton(
                    onClick = onEndClick,
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Selesai", modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}
