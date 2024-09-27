package com.dev.philo.fillsketch.feature.drawing.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.DrawingResultRepository
import com.dev.philo.fillsketch.core.designsystem.utils.createPath
import com.dev.philo.fillsketch.core.designsystem.utils.getEmptyBitmapBySize
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.model.DrawingResultUiState
import com.dev.philo.fillsketch.feature.drawing.model.MyWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingResultViewModel @Inject constructor(
    private val drawingResultRepository: DrawingResultRepository
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()

    private val _drawingResultUiState = MutableStateFlow(DrawingResultUiState())
    val drawingResultUiState = _drawingResultUiState.asStateFlow()

    fun fetchDrawingUiState(drawingResultId: Int) {
        viewModelScope.launch {
            val myWork =
                MyWork.create(drawingResultRepository.getDrawingResult(drawingResultId).first())
            _drawingResultUiState.update {
                it.copy(
                    paths = myWork.paths.toPersistentList()
                )
            }
        }
    }

    fun updateSaveCompleteDialogVisible(state: Boolean) {
        _drawingResultUiState.update {
            it.copy(
                saveCompleteDialogVisible = state
            )
        }
    }

    fun getResultBitmap(
        recommendImageBitmap: ImageBitmap,
        outlineImageBitmap: ImageBitmap
    ): Bitmap {
        val recommendAndroidBitmap = recommendImageBitmap.asAndroidBitmap()
        val outlineAndroidBitmap = outlineImageBitmap.asAndroidBitmap()

        val maskAndroidBitmap = getEmptyBitmapBySize(
            recommendAndroidBitmap.width,
            recommendAndroidBitmap.height,
            recommendAndroidBitmap.density,
            whiteBackground = true
        )

        val maskCanvas = Canvas(maskAndroidBitmap)

        _drawingResultUiState.value.paths.forEach { path ->
            val paint = Paint().apply {

                if (path.actionType == ActionType.MAGIC_BRUSH) {
                    xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                }

                color = if (path.actionType == ActionType.ERASER) {
                    android.graphics.Color.WHITE
                } else {
                    android.graphics.Color.argb(
                        (path.strokeColor.alpha * 255).toInt(),
                        (path.strokeColor.red * 255).toInt(),
                        (path.strokeColor.green * 255).toInt(),
                        (path.strokeColor.blue * 255).toInt()
                    )
                }
                strokeWidth = path.strokeWidth
                style = Style.STROKE
                isAntiAlias = true

                strokeCap = Paint.Cap.ROUND
            }

            val pathObj = createPath(path.points.map { Offset(it.x, it.y) })

            maskCanvas.drawPath(pathObj, paint)
        }

        val resultBitmap = getEmptyBitmapBySize(
            recommendAndroidBitmap.width,
            recommendAndroidBitmap.height,
            recommendAndroidBitmap.density,
            whiteBackground = true
        )
        val resultCanvas = Canvas(resultBitmap)
        val paint = Paint()

        resultCanvas.drawBitmap(recommendAndroidBitmap, 0f, 0f, paint)
        resultCanvas.drawBitmap(maskAndroidBitmap, 0f, 0f, paint)
        resultCanvas.drawBitmap(outlineAndroidBitmap, 0f, 0f, paint)

        return resultBitmap
    }
}