package com.example.projectakhir.ui.components

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
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.LightState
import com.example.projectakhir.domain.model.StudySession
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SessionDetailCard(
    session: StudySession,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy • HH:mm", Locale.getDefault())
    val startDateStr = dateFormat.format(Date(session.startedAt))

    val min = session.actualDurationSec / 60
    val sec = session.actualDurationSec % 60
    val durationLabel = if (min > 0) "${min}m ${sec}s" else "${sec}s"
    val plannedMin = session.plannedDurationSec / 60

    val lightState = LightState.fromLux(session.avgLux)

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
                .padding(20.dp)
        ) {
            // Header: Title, Date, Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Detail Sesi Fokus",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = startDateStr,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(status = session.status)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Time Stats
            DetailSectionHeader(icon = Icons.Default.Timer, title = "Ringkasan Waktu Fokus")
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(label = "Durasi Terjalani", value = durationLabel, isHighlight = true)
            DetailRow(label = "Target Durasi Sesi", value = "$plannedMin Menit")

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Light Sensor Summary
            DetailSectionHeader(icon = Icons.Default.LightMode, title = "Kondisi Cahaya Lingkungan")
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(
                label = "Rata-rata Cahaya",
                value = if (session.avgLux != null) "${session.avgLux.toInt()} Lux (${lightState.label})" else "Tidak Tersedia"
            )
            DetailRow(
                label = "Rentang Cahaya (Min - Maks)",
                value = if (session.minLux != null && session.maxLux != null) "${session.minLux.toInt()} - ${session.maxLux.toInt()} Lux" else "Tidak Tersedia"
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Motion Sensor Summary
            DetailSectionHeader(icon = Icons.Default.MotionPhotosOn, title = "Aktivitas Gerakan Perangkat")
            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(label = "Jumlah Event Gerakan", value = "${session.movementEvents} Event")
            DetailRow(label = "Estimasi Durasi Waktu Bergerak", value = "${session.movementDurationSec} Detik")

            // Section 4: User Note
            if (!session.note.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))

                DetailSectionHeader(icon = Icons.Default.Notes, title = "Catatan Pengguna")
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(14.dp)
                ) {
                    Text(
                        text = "\"${session.note}\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
