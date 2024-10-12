package com.dev.philo.fillsketch.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dev.philo.fillsketch.core.database.entity.SketchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SketchEntityDao {
    @Insert
    suspend fun insert(sketchEntity: SketchEntity)

    @Query("SELECT * FROM sketch")
    fun getAllSketch(): Flow<List<SketchEntity>>

    @Query("SELECT * FROM sketch WHERE sketchType = :sketchType LIMIT 1")
    fun getSketchBySketchType(sketchType: Int): Flow<SketchEntity>

    @Update
    suspend fun update(sketchEntity: SketchEntity)

    @Query("DELETE FROM sketch")
    suspend fun deleteAllSketch()
}