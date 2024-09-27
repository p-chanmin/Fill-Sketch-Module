package com.dev.philo.fillsketch.feature.home.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import com.dev.philo.fillsketch.core.model.DrawingResult

data class MyWork(
    val id: Int = 0,
    val sketchType: Int = 0,
    val paths: SnapshotStateList<PathWrapper> = mutableStateListOf()
) {
    companion object {
        fun create(drawingResult: DrawingResult): MyWork = MyWork(
            id = drawingResult.id,
            sketchType = drawingResult.sketchType,
            paths = drawingResult.paths.map { pathData ->
                PathWrapper(
                    points = pathData.points.map { Offset(it.x, it.y) }.toMutableStateList(),
                    strokeWidth = pathData.strokeWidth,
                    strokeColor = pathData.strokeColor.let { Color(it.r, it.g, it.b, it.alpha) },
                    actionType = pathData.actionType,
                )
            }.toMutableStateList()
        )
    }
}