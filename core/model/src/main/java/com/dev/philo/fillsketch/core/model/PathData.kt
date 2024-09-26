package com.dev.philo.fillsketch.core.model

data class Point(
    val x: Float,
    val y: Float
)

data class StrokeColor(
    val r: Int,
    val g: Int,
    val b: Int,
)

data class PathData(
    val points: List<Point>,
    val strokeWidth: Float = 10f,
    val strokeColor: StrokeColor,
    val actionType: ActionType,
    val alpha: Float = 1f
)