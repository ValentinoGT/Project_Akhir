package com.example.projectakhir.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.StudySession
import com.example.projectakhir.ui.components.AppHeader
import com.example.projectakhir.ui.components.EmptyState
import com.example.projectakhir.ui.components.SessionHistoryItem
import com.example.projectakhir.ui.viewmodel.MainUiState

@Composable
fun HistoryScreen(
    uiState: MainUiState,
    onSessionClick: (StudySession) -> Unit,
    modifier: Modifier = Modifier
) {
    val sessions = uiState.sessions

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        AppHeader(
            title = "Riwayat Fokus",
            subtitle = "Daftar seluruh sesi belajar yang telah diselesaikan"
        )

        if (sessions.isEmpty()) {
            EmptyState(
                title = "Belum Ada Riwayat",
                description = "Sesi belajar yang Anda selesaikan akan muncul di sini."
            )
        } else {
            Text(
                text = "${sessions.size} Sesi Tercatat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(sessions, key = { it.id }) { session ->
                    SessionHistoryItem(
                        session = session,
                        onClick = { onSessionClick(session) }
                    )
                }
            }
        }
    }
}
