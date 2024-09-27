package com.dev.philo.fillsketch.feature.drawing.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.dev.philo.fillsketch.core.model.ActionType

@Immutable
data class DrawingUiState(
    val drawingResultId: Int = 0,
    val sketchType: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val dpi: Int = 0,
    val strokeWidth: Float = 20f,
    val strokeColor: Color = Color(0f, 0f, 0f, 1f),
    val actionType: ActionType = ActionType.MOVE
)
