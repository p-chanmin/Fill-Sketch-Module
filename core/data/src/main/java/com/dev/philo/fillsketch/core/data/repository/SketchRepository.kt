package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.dev.philo.fillsketch.core.model.DrawingResult
import com.dev.philo.fillsketch.core.model.Sketch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SketchRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {
    fun getSketch(): Flow<List<Sketch>> = fillSketchDataSource.sketchList
        .map { sketchSchemaList ->
            sketchSchemaList.map {
                Sketch(
                    sketchType = it.sketchType,
                    hasMagicBrush = it.hasMagicBrush,
                    isLocked = it.isLocked
                )
            }
        }

    fun getDrawingResult(): Flow<List<DrawingResult>> =
        fillSketchDataSource.drawingResultList
            .map { drawingResultList ->
                drawingResultList.map {
                    DrawingResult(
                        id = it.id,
                        sketchType = it.sketchType,
                        latestMaskBitmapByteArray = it.latestMaskBitmapByteArray,
                        resultBitmapByteArray = it.resultBitmapByteArray
                    )
                }
            }

    fun getDrawingResultBySketchType(sketchType: Int): Flow<List<DrawingResult>> =
        fillSketchDataSource.drawingResultList
            .map { myWorkSchemaList ->
                myWorkSchemaList.filter { it.sketchType == sketchType }.map {
                    DrawingResult(
                        id = it.id,
                        sketchType = it.sketchType,
                        latestMaskBitmapByteArray = it.latestMaskBitmapByteArray,
                        resultBitmapByteArray = it.resultBitmapByteArray
                    )
                }
            }

    suspend fun addDrawingResult(sketchType: Int, latestByteArray: ByteArray): Long {
        return fillSketchDataSource.addDrawingResult(sketchType, latestByteArray)
    }

    suspend fun deleteDrawingResult(drawingResultId: Long) {
        fillSketchDataSource.deleteDrawingResult(drawingResultId)
    }

    suspend fun updateLockState(sketchType: Int, isLocked: Boolean = false) {
        fillSketchDataSource.updateLockState(sketchType, isLocked)
    }
}