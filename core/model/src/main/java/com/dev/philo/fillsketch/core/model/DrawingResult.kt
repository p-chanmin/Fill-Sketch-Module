package com.dev.philo.fillsketch.core.model

data class DrawingResult(
    val id: Int = 0,
    val sketchType: Int = 0,
    val latestMaskBitmapByteArray: ByteArray = ByteArray(0),
    val resultBitmapByteArray: ByteArray = ByteArray(0),
    val hasMagicBrush: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DrawingResult

        if (id != other.id) return false
        if (sketchType != other.sketchType) return false
        if (!latestMaskBitmapByteArray.contentEquals(other.latestMaskBitmapByteArray)) return false
        if (!resultBitmapByteArray.contentEquals(other.resultBitmapByteArray)) return false
        if (hasMagicBrush != other.hasMagicBrush) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + sketchType
        result = 31 * result + latestMaskBitmapByteArray.contentHashCode()
        result = 31 * result + resultBitmapByteArray.contentHashCode()
        result = 31 * result + hasMagicBrush.hashCode()
        return result
    }
}