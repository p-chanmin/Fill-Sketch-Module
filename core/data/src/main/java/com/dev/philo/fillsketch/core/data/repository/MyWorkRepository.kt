package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.data.converter.PathWrapperAdapter
import com.dev.philo.fillsketch.core.data.model.MyWork
import com.dev.philo.fillsketch.core.data.model.PathWrapper
import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyWorkRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(PathWrapper::class.java, PathWrapperAdapter())
        .create()
    private val type = object : TypeToken<List<PathWrapper>>() {}.type

    fun getMyWork(myWorkId: Int): Flow<MyWork> = fillSketchDataSource
        .getMyWorkById(myWorkId)
        .map {
            val pathWrapperList: List<PathWrapper> = gson.fromJson(it.pathsJsonData, type)
            MyWork(
                id = it._id,
                sketchType = it.sketchType,
                paths = pathWrapperList
            )
        }

    suspend fun updateMagicBrushState(sketchType: Int, hasMagicBrush: Boolean = true) {
        fillSketchDataSource.updateMagicBrushState(sketchType, hasMagicBrush)
    }

    suspend fun updateMyWork(
        myWorkId: Int,
        paths: List<PathWrapper>
    ) {
        fillSketchDataSource.updateMyWork(myWorkId, gson.toJson(paths))
    }
}