package com.dev.philo.fillsketch.core.model

data class DrawingResult (
    val id: Int = 0,
    val sketchType: Int = 0,
    val paths: List<PathData> = listOf(),
    val hasMagicBrush: Boolean = false
)