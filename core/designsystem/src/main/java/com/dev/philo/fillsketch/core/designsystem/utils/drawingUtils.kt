package com.dev.philo.fillsketch.core.designsystem.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.geometry.Offset

private fun calculateMidpoint(start: Offset, end: Offset) =
    Offset((start.x + end.x) / 2, (start.y + end.y) / 2)

fun createPath(points: List<Offset>) = Path().apply {
    if (points.size > 1) {
        this.moveTo(points[0].x, points[0].y)
        if (points.size == 2) {
            this.lineTo(points[1].x, points[1].y)
        } else {
            for (i in 2 until points.size) {
                val midPoint = calculateMidpoint(points[i - 1], points[i])
                quadTo(points[i - 1].x, points[i - 1].y, midPoint.x, midPoint.y)
            }
        }
    }
}

fun getEmptyBitmapBySize(width: Int, height: Int, dpi: Int, whiteBackground: Boolean): Bitmap {
    return Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888,
    ).apply {
        density = dpi
        if (whiteBackground) {
            val canvas = Canvas(this)
            val paint = Paint().apply {
                color = android.graphics.Color.WHITE
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }
}