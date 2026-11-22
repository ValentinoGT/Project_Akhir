package com.example.projectakhir.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = OnPrimaryLight,
    primaryContainer = EmeraldContainerLight,
    onPrimaryContainer = OnEmeraldContainerLight,
    secondary = EmeraldLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = OnPrimaryDark,
    primaryContainer = EmeraldContainerDark,
    onPrimaryContainer = OnEmeraldContainerDark,
    secondary = EmeraldPrimary,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = BorderDark
)

@Composable
fun FocusSenseTheme(
    themeMode: String = "SYSTEM",
    darkTheme: Boolean = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemInDarkTheme()
    },
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
