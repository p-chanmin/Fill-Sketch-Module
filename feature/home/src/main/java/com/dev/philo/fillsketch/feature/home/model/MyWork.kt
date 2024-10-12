package com.dev.philo.fillsketch.feature.home.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.dev.philo.fillsketch.core.model.DrawingResult

data class MyWork(
    val id: Long = 0,
    val sketchType: Int = 0,
    val resultBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val hasMagicBrush: Boolean = false
) {
    companion object {
        fun create(drawingResult: DrawingResult): MyWork {
            return MyWork(
                id = drawingResult.id,
                sketchType = drawingResult.sketchType,
                resultBitmap = byteArrayToBitmap(drawingResult.resultBitmapByteArray),
                hasMagicBrush = drawingResult.hasMagicBrush
            )
        }

        private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
            return if (byteArray.isEmpty()) {
                Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            } else {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }
        }
    }
}