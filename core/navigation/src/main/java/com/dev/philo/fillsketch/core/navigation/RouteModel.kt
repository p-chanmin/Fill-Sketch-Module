package com.dev.philo.fillsketch.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object SketchList : Route

    @Serializable
    data object MyWorks : Route

    @Serializable
    data class Drawing(val sketchType: Int, val drawingResultId: Int) : Route

    @Serializable
    data class DrawingResult(val sketchType: Int, val drawingResultId: Int) : Route
}