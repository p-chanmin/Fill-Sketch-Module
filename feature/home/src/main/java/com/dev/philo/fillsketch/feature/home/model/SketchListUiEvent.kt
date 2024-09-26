package com.dev.philo.fillsketch.feature.home.model

sealed interface SketchListUiEvent {
    data class NavigateToDrawing(val sketchType: Int, val drawingResultId: Int) : SketchListUiEvent
}