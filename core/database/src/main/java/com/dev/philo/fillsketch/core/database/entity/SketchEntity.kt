package com.dev.philo.fillsketch.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sketch")
data class SketchEntity(
    @PrimaryKey
    val sketchType: Int = -1,
    val hasMagicBrush: Boolean = false,
    val isLocked: Boolean = true,
)