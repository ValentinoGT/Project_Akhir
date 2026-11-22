package com.example.projectakhir.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MotionPhotosOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.LightState
import com.example.projectakhir.domain.model.MovementState
import java.util.Locale

@Composable
fun SensorStatusCard(
    currentLux: Float?,
    lightState: LightState,
    isLightAvailable: Boolean,
    currentMagnitude: Float?,
    movementState: MovementState,
    isMotionAvailable: Boolean,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onDetailsClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kondisi Lingkungan & Sensor",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Detail >",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SensorItem(
                    icon = Icons.Default.LightMode,
                    title = "Cahaya",
                    value = if (isLightAvailable && currentLux != null) "${currentLux.toInt()} Lux" else "Tidak Tersedia",
                    statusLabel = if (isLightAvailable) lightState.label else "Tidak Tersedia",
                    isAvailable = isLightAvailable,
                    modifier = Modifier.weight(1f)
                )

                SensorItem(
                    icon = Icons.Default.MotionPhotosOn,
                    title = "Gerakan",
                    value = if (isMotionAvailable && currentMagnitude != null) String.format(Locale.getDefault(), "%.2f m/s²", currentMagnitude) else "Tidak Tersedia",
                    statusLabel = if (isMotionAvailable) movementState.label else "Tidak Tersedia",
                    isAvailable = isMotionAvailable,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SensorItem(
    icon: ImageVector,
    title: String,
    value: String,
    statusLabel: String,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isAvailable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 6.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = statusLabel,
            style = MaterialTheme.typography.labelSmall,
            color = if (isAvailable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}
