package com.example.projectakhir.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.ui.components.AppHeader
import com.example.projectakhir.ui.viewmodel.MainUiState
import java.util.Locale

@Composable
fun SensorDetailsScreen(
    uiState: MainUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val lightData = uiState.lightData
    val motionData = uiState.motionData

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            AppHeader(
                title = "Detail Sensor",
                subtitle = "Informasi pembacaan hardware & algoritma klasifikasi",
                trailing = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Light Sensor Card Detail
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Light Sensor (Cahaya Lingkungan)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "API Android: Sensor.TYPE_LIGHT (Lux)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (lightData.isAvailable) {
                        DetailRow(
                            label = "Lux Saat Ini",
                            value = if (lightData.currentLux != null) "${lightData.currentLux.toInt()} Lux" else "-"
                        )
                        DetailRow(
                            label = "Minimum Lux",
                            value = if (lightData.minLux != null) "${lightData.minLux.toInt()} Lux" else "-"
                        )
                        DetailRow(
                            label = "Maksimum Lux",
                            value = if (lightData.maxLux != null) "${lightData.maxLux.toInt()} Lux" else "-"
                        )
                        DetailRow(
                            label = "Rata-rata Lux",
                            value = if (lightData.avgLux != null) "${lightData.avgLux.toInt()} Lux" else "-"
                        )
                        DetailRow(
                            label = "Status Klasifikasi",
                            value = lightData.lightState.label
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = lightData.lightState.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    } else {
                        Text(
                            text = "Hardware Light Sensor tidak tersedia pada perangkat ini. Sistem timer & database tetap berfungsi secara offline.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Motion Sensor Card Detail
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Position Sensor (Akselerometer)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "API Android: Sensor.TYPE_ACCELEROMETER (m/s²)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (motionData.isAvailable) {
                        DetailRow(
                            label = "Magnitudo Akselerasi",
                            value = if (motionData.currentMagnitude != null) String.format(
                                Locale.getDefault(),
                                "%.2f m/s²",
                                motionData.currentMagnitude
                            ) else "-"
                        )
                        DetailRow(
                            label = "Status Gerakan",
                            value = motionData.movementState.label
                        )
                        DetailRow(
                            label = "Total Event Gerakan (Debounced 1.5s)",
                            value = "${motionData.movementEventsCount} Event"
                        )
                        DetailRow(
                            label = "Estimasi Durasi Waktu Bergerak",
                            value = "${motionData.movementDurationSec} Detik"
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = motionData.movementState.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    } else {
                        Text(
                            text = "Hardware Accelerometer tidak tersedia pada perangkat ini. Fallback status: UNKNOWN.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
