package com.dev.philo.fillsketch.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dev.philo.fillsketch.core.database.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingEntityDao {
    @Insert
    suspend fun insert(settingEntity: SettingEntity)

    @Query("SELECT * FROM settings WHERE id = 0")
    fun getSettings(): Flow<SettingEntity?>

    @Update
    suspend fun update(settingEntity: SettingEntity)
}