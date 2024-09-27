package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.data.converter.PathDataAdapter
import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.dev.philo.fillsketch.core.model.DrawingResult
import com.dev.philo.fillsketch.core.model.PathData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DrawingResultRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(PathData::class.java, PathDataAdapter())
        .create()
    private val type = object : TypeToken<List<PathData>>() {}.type

    fun getDrawingResult(sketchType: Int, drawingResultId: Int): Flow<DrawingResult> =
        fillSketchDataSource
            .getDrawingResultById(drawingResultId)
            .map {
                val pathDataList: List<PathData> = gson.fromJson(it.pathsJsonData, type)
                DrawingResult(
                    id = it._id,
                    sketchType = it.sketchType,
                    paths = pathDataList,
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
        paths: List<PathData>
    ) {
        fillSketchDataSource.updateDrawingResult(drawingResultId, gson.toJson(paths))
    }
}