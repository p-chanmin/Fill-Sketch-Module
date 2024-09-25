package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.data.converter.PathWrapperAdapter
import com.dev.philo.fillsketch.core.data.model.MyWork
import com.dev.philo.fillsketch.core.data.model.PathWrapper
import com.dev.philo.fillsketch.core.data.model.Sketch
import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SketchRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(PathWrapper::class.java, PathWrapperAdapter())
        .create()
    private val type = object : TypeToken<List<PathWrapper>>() {}.type

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

    fun getMyWorkBySketchType(sketchType: Int): Flow<List<MyWork>> =
        fillSketchDataSource.myWorksList
            .map { myWorkSchemaList ->
                myWorkSchemaList.map {
                    val pathWrapperList: List<PathWrapper> = gson.fromJson(it.pathsJsonData, type)
                    MyWork(
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
        fillSketchDataSource.deleteMyWork(myWorkId)
    }

    suspend fun updateLockState(sketchType: Int, isLocked: Boolean = false) {
        fillSketchDataSource.updateLockState(sketchType, isLocked)
    }
}