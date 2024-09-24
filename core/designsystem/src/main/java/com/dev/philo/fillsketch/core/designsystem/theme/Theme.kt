package com.dev.philo.fillsketch.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ColorScheme = lightColorScheme(
    primary = Sunset,
    onPrimary = White,
    primaryContainer = Aero,
    onPrimaryContainer = Black,
    background = TiffanyBlue,
    onBackground = White,
    onSurface = Aquamarine,
    surfaceContainer = TiffanyBlue,
    secondary = Khaki,
    onSecondary = Black,
    secondaryContainer = Dun,
    onSecondaryContainer = Black,
    tertiary = SandyBrown1,
    onTertiary = Black,
    outline = SandyBronw2,
    scrim = Black.copy(alpha = 0.30f)
)

@Composable
fun FillSketchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content
    )
}