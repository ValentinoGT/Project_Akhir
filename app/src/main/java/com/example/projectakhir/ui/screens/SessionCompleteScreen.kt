package com.example.projectakhir.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.SessionStatus
import com.example.projectakhir.ui.components.SessionDetailCard
import com.example.projectakhir.ui.viewmodel.MainUiState

@Composable
fun SessionCompleteScreen(
    uiState: MainUiState,
    onSaveClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val session = uiState.lastCompletedSession
    var noteText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selesai",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = if (session?.status == SessionStatus.COMPLETED) "Sesi Fokus Selesai!" else "Sesi Dibatalkan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kerja bagus! Sesi Anda telah berhasil dicatat ke database.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (session != null) {
                SessionDetailCard(session = session)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Tambahkan Catatan (Opsional)") },
                placeholder = { Text("Contoh: Belajar Bab 3 Matematika") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onSaveClick(noteText.ifBlank { null }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Simpan & Kembali ke Home",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
