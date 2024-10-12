package com.dev.philo.fillsketch.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dev.philo.fillsketch.core.database.entity.DrawingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawingEntityDao {
    @Insert
    suspend fun insert(drawingEntity: DrawingEntity): Long

    @Query("SELECT * FROM drawing")
    fun getAllDrawing(): Flow<List<DrawingEntity>>

    @Query("SELECT * FROM drawing WHERE id = :id LIMIT 1")
    fun getDrawingById(id: Long): Flow<DrawingEntity>

    @Update
    suspend fun update(drawingEntity: DrawingEntity)

    @Query("DELETE FROM drawing WHERE id = :id")
    suspend fun deleteDrawingById(id: Long)
}