package com.dev.philo.fillsketch.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey
    val id: Int = 0,
    val soundEffect: Boolean = true,
    val backgroundMusic: Boolean = true,
)