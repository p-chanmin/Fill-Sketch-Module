package com.dev.philo.fillsketch.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drawing")
data class DrawingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sketchType: Int = -1,
    val latestMaskBitmapByteArray: ByteArray = ByteArray(0),
    val resultBitmapByteArray: ByteArray = ByteArray(0),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DrawingEntity

        if (id != other.id) return false
        if (sketchType != other.sketchType) return false
        if (!latestMaskBitmapByteArray.contentEquals(other.latestMaskBitmapByteArray)) return false
        if (!resultBitmapByteArray.contentEquals(other.resultBitmapByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + sketchType
        result = 31 * result + latestMaskBitmapByteArray.contentHashCode()
        result = 31 * result + resultBitmapByteArray.contentHashCode()
        return result
    }
}