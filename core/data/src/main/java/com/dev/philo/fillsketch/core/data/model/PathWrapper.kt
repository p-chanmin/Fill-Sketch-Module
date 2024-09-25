package com.dev.philo.fillsketch.core.data.model

data class Offset(
    val x: Float,
    val y: Float
)

data class ColorSet(
    val r: Int,
    val g: Int,
    val b: Int,
)

data class PathWrapper(
    var points: List<Offset>,
    val strokeWidth: Float = 10f,
    val strokeColor: ColorSet,
    val actionType: ActionType,
    val alpha: Float = 1f
)