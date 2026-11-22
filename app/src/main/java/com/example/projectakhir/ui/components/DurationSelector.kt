package com.example.projectakhir.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DurationSelector(
    selectedDurationSec: Long,
    onDurationSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf(
        15 * 60L to "15m",
        25 * 60L to "25m",
        45 * 60L to "45m",
        60 * 60L to "60m"
    )

    var showCustomDialog by remember { mutableStateOf(false) }
    var customMinutesInput by remember { mutableStateOf("") }
    val isCustomSelected = presets.none { it.first == selectedDurationSec }
    val selectedMinutesLabel = if (isCustomSelected) "${selectedDurationSec / 60}m (Kustom)" else null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Pilih Durasi Fokus",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            presets.forEach { (sec, label) ->
                val selected = selectedDurationSec == sec
                FilterChip(
                    selected = selected,
                    onClick = { onDurationSelected(sec) },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            FilterChip(
                selected = isCustomSelected,
                onClick = {
                    customMinutesInput = (selectedDurationSec / 60).toString()
                    showCustomDialog = true
                },
                label = { Text(selectedMinutesLabel ?: "Kustom...") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }

    if (showCustomDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("Atur Durasi Kustom") },
            text = {
                Column {
                    Text(
                        text = "Masukkan target durasi sesi belajar dalam satuan menit (contoh: 5, 10, 30, 90):",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = customMinutesInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() } && input.length <= 4) {
                                customMinutesInput = input
                            }
                        },
                        label = { Text("Durasi (Menit)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val min = customMinutesInput.toLongOrNull()
                        if (min != null && min > 0) {
                            onDurationSelected(min * 60L)
                            showCustomDialog = false
                        }
                    }
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCustomDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
