package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.data.converter.PathDataAdapter
import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.dev.philo.fillsketch.core.model.DrawingResult
import com.dev.philo.fillsketch.core.model.PathData
import com.dev.philo.fillsketch.core.model.Sketch
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SketchRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(PathData::class.java, PathDataAdapter())
        .create()
    private val type = object : TypeToken<List<PathData>>() {}.type

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
                        id = it._id,
                        sketchType = it.sketchType,
                        bitmapByteArray = it.bitmapByteArray
                    )
                }
            }

    fun getDrawingResultBySketchType(sketchType: Int): Flow<List<DrawingResult>> =
        fillSketchDataSource.drawingResultList
            .map { myWorkSchemaList ->
                myWorkSchemaList.filter { it.sketchType == sketchType }.map {
                    DrawingResult(
                        id = it._id,
                        sketchType = it.sketchType,
                        bitmapByteArray = it.bitmapByteArray
                    )
                }
            }

    suspend fun addDrawingResult(sketchType: Int, latestByteArray: ByteArray): Int {
        return fillSketchDataSource.addDrawingResult(sketchType, latestByteArray)
    }

    suspend fun deleteDrawingResult(drawingResultId: Int) {
        fillSketchDataSource.deleteDrawingResult(drawingResultId)
    }

    suspend fun updateLockState(sketchType: Int, isLocked: Boolean = false) {
        fillSketchDataSource.updateLockState(sketchType, isLocked)
    }
}