package com.example.projectakhir.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.ui.components.AppHeader
import com.example.projectakhir.ui.viewmodel.MainUiState

@Composable
fun SettingsScreen(
    uiState: MainUiState,
    onThemeModeChange: (String) -> Unit,
    onNotificationChange: (Boolean) -> Unit,
    onVibrationChange: (Boolean) -> Unit,
    onDeleteAllHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AppHeader(
            title = "Pengaturan",
            subtitle = "Konfigurasi tampilan, preferensi & izin sensor"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // A. Appearance Theme Group
        SettingsCardGroup(title = "Tampilan & Tema") {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = "Tema",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Mode Tema Aplikasi",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "SYSTEM" to "Sistem",
                    "LIGHT" to "Terang",
                    "DARK" to "Gelap"
                ).forEach { (modeKey, label) ->
                    val selected = uiState.themeMode == modeKey
                    FilterChip(
                        selected = selected,
                        onClick = { onThemeModeChange(modeKey) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // B. Preferences & Notifications Group
        SettingsCardGroup(title = "Pengingat & Notifikasi") {
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Notifikasi Sesi Selesai",
                subtitle = "Tampilkan notifikasi saat target timer tercapai",
                checked = uiState.notificationEnabled,
                onCheckedChange = onNotificationChange
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            SettingsSwitchItem(
                icon = Icons.Default.Vibration,
                title = "Getaran Sesi Selesai",
                subtitle = "Aktifkan efek getar saat sesi fokus berakhir",
                checked = uiState.vibrationEnabled,
                onCheckedChange = onVibrationChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // C. Sensor & Data Management Group
        SettingsCardGroup(title = "Hardware Sensor & Data") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sensors,
                    contentDescription = "Sensor Status",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column {
                    Text(
                        text = "Status Hardware Sensor",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Cahaya: ${if (uiState.lightData.isAvailable) "Tersedia" else "Tidak Ada"} | Gerakan: ${if (uiState.motionData.isAvailable) "Tersedia" else "Tidak Ada"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Hapus Riwayat",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hapus Seluruh Riwayat",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Mengosongkan seluruh catatan sesi dari database.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.DeleteSweep, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kosongkan Riwayat Database")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // D. About Group
        SettingsCardGroup(title = "Tentang FocusSense") {
            Text(
                text = "FocusSense 2.0",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Aplikasi pemantau produktivitas berbasis waktu dan kondisi sensor lingkungan (Light & Motion) dengan prinsip offline-first.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Konfirmasi Hapus Seluruh Riwayat") },
            text = { Text("Apakah Anda yakin ingin menghapus seluruh data sesi dari database? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteAllHistoryClick()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus Semua")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun SettingsCardGroup(
    title: String,
    content: @Composable () -> Unit
) {
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
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
