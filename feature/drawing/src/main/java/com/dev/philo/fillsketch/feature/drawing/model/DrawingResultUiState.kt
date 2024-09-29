package com.dev.philo.fillsketch.feature.drawing.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable

@Immutable
data class DrawingResultUiState(
    val isLoading: Boolean = true,
    val resultBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val saveCompleteDialogVisible: Boolean = false,
)
