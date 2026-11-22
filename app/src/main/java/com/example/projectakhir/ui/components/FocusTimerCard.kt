package com.example.projectakhir.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FocusTimerCard(
    remainingSeconds: Long,
    targetSeconds: Long,
    isRunning: Boolean,
    isPaused: Boolean,
    isIdle: Boolean,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onResumeClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (targetSeconds > 0) {
        val elapsed = targetSeconds - remainingSeconds
        (elapsed.toFloat() / targetSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    val strokeWidth = 12.dp.toPx()
                    drawCircle(
                        color = trackColor,
                        style = Stroke(width = strokeWidth)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = progress * 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatTime(remainingSeconds),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when {
                            isRunning -> "Fokus Sesi Berjalan"
                            isPaused -> "Sesi Dipause"
                            else -> "Siap Belajar"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isIdle) {
                    Button(
                        onClick = onStartClick,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Mulai")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mulai Fokus", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    if (isRunning) {
                        Button(
                            onClick = onPauseClick,
                            shape = CircleShape,
                            modifier = Modifier.size(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.Pause, contentDescription = "Pause")
                        }
                    } else if (isPaused) {
                        Button(
                            onClick = onResumeClick,
                            shape = CircleShape,
                            modifier = Modifier.size(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Lanjutkan")
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedButton(
                        onClick = onEndClick,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Selesai")
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
