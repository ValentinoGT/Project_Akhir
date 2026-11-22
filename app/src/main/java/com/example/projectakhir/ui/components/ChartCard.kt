package com.example.projectakhir.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projectakhir.domain.model.DailyBreakdown

@Composable
fun ChartCard(
    dailyBreakdown: List<DailyBreakdown>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val barBackgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)

    val maxSeconds = dailyBreakdown.maxOfOrNull { it.totalSeconds }?.coerceAtLeast(1800L) ?: 1800L
    val total7DaysSec = dailyBreakdown.sumOf { it.totalSeconds }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grafik Produktivitas 7 Hari",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${total7DaysSec / 60} Min Total",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (total7DaysSec == 0L) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada aktivitas fokus tercatat 7 hari terakhir.\nMulai sesi belajar untuk melihat statistik harian Anda.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val count = dailyBreakdown.size.coerceAtLeast(1)
                    val barWidth = (canvasWidth / count) * 0.42f
                    val spacing = (canvasWidth - (barWidth * count)) / (count + 1)

                    dailyBreakdown.forEachIndexed { index, breakdown ->
                        val x = spacing + index * (barWidth + spacing)
                        val fillHeight = (breakdown.totalSeconds.toFloat() / maxSeconds.toFloat()) * canvasHeight

                        // Background track bar
                        drawRoundRect(
                            color = barBackgroundColor,
                            topLeft = Offset(x, 0f),
                            size = Size(barWidth, canvasHeight),
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )

                        // Active progress bar
                        if (fillHeight > 0f) {
                            drawRoundRect(
                                color = primaryColor,
                                topLeft = Offset(x, canvasHeight - fillHeight),
                                size = Size(barWidth, fillHeight),
                                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dailyBreakdown.forEach { breakdown ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${breakdown.totalSeconds / 60}m",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (breakdown.totalSeconds > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = breakdown.dayName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
