package com.example.projectakhir.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projectakhir.ui.components.AppHeader
import com.example.projectakhir.ui.components.ChartCard
import com.example.projectakhir.ui.components.StatisticCard
import com.example.projectakhir.ui.viewmodel.MainUiState

@Composable
fun InsightsScreen(
    uiState: MainUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val stats = uiState.statistics

    val todayMin = (stats?.todayTotalSeconds ?: 0L) / 60
    val sevenDaysMin = (stats?.sevenDaysTotalSeconds ?: 0L) / 60
    val avgMin = (stats?.averageDurationSeconds ?: 0L) / 60
    val sevenDaysCount = stats?.sevenDaysSessionCount ?: 0

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AppHeader(
            title = "Analisis & Insights",
            subtitle = "Ringkasan produktivitas & tren sesi fokus Anda"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of 4 Stat Summary Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatisticCard(
                    title = "Hari Ini",
                    value = "$todayMin Menit",
                    subtitle = "${stats?.todaySessionCount ?: 0} sesi selesai",
                    icon = Icons.Default.Timer,
                    modifier = Modifier.weight(1f)
                )
                StatisticCard(
                    title = "7 Hari Terakhir",
                    value = "$sevenDaysMin Menit",
                    subtitle = "$sevenDaysCount sesi total",
                    icon = Icons.Default.DateRange,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatisticCard(
                    title = "Rata-rata Sesi",
                    value = "$avgMin Menit",
                    subtitle = "Per sesi selesai",
                    icon = Icons.Default.HourglassTop,
                    modifier = Modifier.weight(1f)
                )
                StatisticCard(
                    title = "Status Sensor",
                    value = if (uiState.lightData.isAvailable) "Aktif" else "Terbatas",
                    subtitle = uiState.lightData.lightState.label,
                    icon = Icons.Default.LightMode,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 7-day Breakdown Bar Chart
        if (stats?.dailyBreakdown != null) {
            ChartCard(dailyBreakdown = stats.dailyBreakdown)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
