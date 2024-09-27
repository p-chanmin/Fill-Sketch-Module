package com.dev.philo.fillsketch.feature.drawing.model

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.component.presetColorList

@Immutable
data class DrawingUiState(
    val drawingResultId: Int = 0,
    val sketchType: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val dpi: Int = 0,
    val latestBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val strokeWidth: Float = 30f,
    val strokeColor: Color = presetColorList[0][0],
    val actionType: ActionType = ActionType.BRUSH,
    val hasMagicBrush: Boolean = false
)
