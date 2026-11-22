package com.example.projectakhir.ui.screens

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.StudySession
import com.example.projectakhir.ui.components.AppHeader
import com.example.projectakhir.ui.components.SessionDetailCard

@Composable
fun SessionDetailsScreen(
    session: StudySession?,
    onBackClick: () -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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
                title = "Detail Sesi",
                subtitle = "Rincian durasi & data lingkungan",
                trailing = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (session != null) {
                SessionDetailCard(session = session)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onDeleteClick(session.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Hapus Sesi Ini", style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
