package com.dev.philo.fillsketch.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dev.philo.fillsketch.core.designsystem.R

private val dunggeunmiso = FontFamily(
    Font(R.font.dunggeunmiso_regular, FontWeight.Normal),
    Font(R.font.dunggeunmiso_bold, FontWeight.Bold),
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold
    ),
    titleSmall = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyLarge = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),
    bodyMedium = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),
    bodySmall = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    ),
    labelLarge = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),
    labelMedium = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal
    ),
    labelSmall = TextStyle(
        fontFamily = dunggeunmiso,
        fontSize = 10.sp,
        fontWeight = FontWeight.Normal
    )
)