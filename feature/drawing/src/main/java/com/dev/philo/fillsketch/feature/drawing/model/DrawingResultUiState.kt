package com.dev.philo.fillsketch.feature.drawing.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DrawingResultUiState(
    val latestBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val saveCompleteDialogVisible: Boolean = false,
)
