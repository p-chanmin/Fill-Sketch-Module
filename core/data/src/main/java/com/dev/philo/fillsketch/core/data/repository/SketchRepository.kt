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

    fun getMyWorkBySketchType(sketchType: Int): Flow<List<DrawingResult>> =
        fillSketchDataSource.drawingResultList
            .map { myWorkSchemaList ->
                myWorkSchemaList.filter { it.sketchType == sketchType }.map {
                    val pathWrapperList: List<PathData> = gson.fromJson(it.pathsJsonData, type)
                    DrawingResult(
                        id = it._id,
                        sketchType = it.sketchType,
                        paths = pathWrapperList
                    )
                }
            }

    suspend fun addMyWork(sketchType: Int): Int {
        return fillSketchDataSource.addMyWork(sketchType)
    }

    suspend fun deleteMyWork(myWorkId: Int) {
        fillSketchDataSource.deleteDrawingResult(myWorkId)
    }

    suspend fun updateLockState(sketchType: Int, isLocked: Boolean = false) {
        fillSketchDataSource.updateLockState(sketchType, isLocked)
    }
}