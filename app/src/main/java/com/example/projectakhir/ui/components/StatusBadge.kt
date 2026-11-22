package com.example.projectakhir.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.SessionStatus
import com.example.projectakhir.ui.theme.StatusError
import com.example.projectakhir.ui.theme.StatusSuccess

@Composable
fun StatusBadge(
    status: SessionStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        SessionStatus.COMPLETED -> Triple(
            StatusSuccess.copy(alpha = 0.15f),
            StatusSuccess,
            "Selesai"
        )
        SessionStatus.ABORTED -> Triple(
            StatusError.copy(alpha = 0.15f),
            StatusError,
            "Dibatalkan"
        )
    }

    Box(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
