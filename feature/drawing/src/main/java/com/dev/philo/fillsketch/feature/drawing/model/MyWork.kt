package com.dev.philo.fillsketch.feature.drawing.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dev.philo.fillsketch.core.model.DrawingResult

data class MyWork(
    val id: Long = 0,
    val sketchType: Int = 0,
    val latestBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val resultBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val hasMagicBrush: Boolean = false
) {
    companion object {
        fun create(drawingResult: DrawingResult, bitmapDpi: Int): MyWork = MyWork(
            id = drawingResult.id,
            sketchType = drawingResult.sketchType,
            latestBitmap = byteArrayToBitmap(drawingResult.latestMaskBitmapByteArray).apply {
                density = bitmapDpi
            },
            resultBitmap = byteArrayToBitmap(drawingResult.resultBitmapByteArray).apply {
                density = bitmapDpi
            },
            hasMagicBrush = drawingResult.hasMagicBrush
        )

        private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return if (byteArray.isEmpty()) {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            } else {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }
        }
    }
}