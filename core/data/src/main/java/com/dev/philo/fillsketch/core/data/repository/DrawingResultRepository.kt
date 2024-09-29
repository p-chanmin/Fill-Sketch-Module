package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.dev.philo.fillsketch.core.model.DrawingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DrawingResultRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {
    fun getDrawingResult(sketchType: Int, drawingResultId: Int): Flow<DrawingResult> =
        fillSketchDataSource
            .getDrawingResultById(drawingResultId)
            .map {
                DrawingResult(
                    id = it._id,
                    sketchType = it.sketchType,
                    latestMaskBitmapByteArray = it.latestMaskBitmapByteArray,
                    resultBitmapByteArray = it.resultBitmapByteArray,
                    hasMagicBrush = fillSketchDataSource.getMagicBrushStateBySketchType(sketchType)
                        .first()
                )
            }

    fun getMagicBrushState(sketchType: Int): Flow<Boolean> =
        fillSketchDataSource.getMagicBrushStateBySketchType(sketchType)

    suspend fun updateMagicBrushState(sketchType: Int, hasMagicBrush: Boolean = true) {
        fillSketchDataSource.updateMagicBrushState(sketchType, hasMagicBrush)
    }

    suspend fun updateDrawingResult(
        drawingResultId: Int,
        latestMaskBitmapByteArray: ByteArray,
        resultBitmapByteArray: ByteArray
    ) {
        fillSketchDataSource.updateDrawingResult(
            drawingResultId = drawingResultId,
            latestMaskBitmapByteArray = latestMaskBitmapByteArray,
            resultBitmapByteArray = resultBitmapByteArray
        )
    }
}