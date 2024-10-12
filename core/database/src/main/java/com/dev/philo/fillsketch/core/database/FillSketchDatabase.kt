package com.dev.philo.fillsketch.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dev.philo.fillsketch.core.database.dao.DrawingEntityDao
import com.dev.philo.fillsketch.core.database.dao.SettingEntityDao
import com.dev.philo.fillsketch.core.database.dao.SketchEntityDao
import com.dev.philo.fillsketch.core.database.entity.DrawingEntity
import com.dev.philo.fillsketch.core.database.entity.SettingEntity
import com.dev.philo.fillsketch.core.database.entity.SketchEntity

@Database(entities = [SettingEntity::class, SketchEntity::class, DrawingEntity::class], version = 1)
abstract class FillSketchDatabase : RoomDatabase() {
    abstract fun settingEntityDao(): SettingEntityDao
    abstract fun sketchEntityDao(): SketchEntityDao
    abstract fun drawingEntityDao(): DrawingEntityDao
}