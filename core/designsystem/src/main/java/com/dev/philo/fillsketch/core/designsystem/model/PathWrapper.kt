package com.dev.philo.fillsketch.core.designsystem.model

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.core.model.PathData
import com.dev.philo.fillsketch.core.model.Point
import com.dev.philo.fillsketch.core.model.StrokeColor

data class PathWrapper(
    var points: SnapshotStateList<Offset>,
    val strokeWidth: Float,
    val strokeColor: Color,
    val actionType: ActionType,
    val alpha: Float
) {
    companion object {
        fun PathWrapper.toPathData(): PathData {
            return PathData(
                points = points.map { Point(it.x, it.y) },
                strokeWidth = strokeWidth,
                strokeColor = StrokeColor(
                    (strokeColor.red * 255).toInt(),
                    (strokeColor.green * 255).toInt(),
                    (strokeColor.blue * 255).toInt(),
                ),
                actionType = actionType,
                alpha = alpha
            )
        }
    }
}