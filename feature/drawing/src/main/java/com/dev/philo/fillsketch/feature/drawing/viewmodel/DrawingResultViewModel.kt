package com.dev.philo.fillsketch.feature.drawing.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.DrawingResultRepository
import com.dev.philo.fillsketch.core.designsystem.utils.getEmptyBitmapBySize
import com.dev.philo.fillsketch.feature.drawing.model.DrawingResultUiState
import com.dev.philo.fillsketch.feature.drawing.model.MyWork
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun fetchDrawingUiState(
        sketchType: Int,
        drawingResultId: Int,
        dpi: Int
    ) {
        viewModelScope.launch {
            val myWork = MyWork.create(
                drawingResultRepository.getDrawingResult(sketchType, drawingResultId).first(),
                dpi
            )
            _drawingResultUiState.update {
                it.copy(
                    isLoading = false,
                    resultBitmap = myWork.resultBitmap
                )
            }
        }
    }

    fun updateSaveCompleteDialogVisible(state: Boolean) {
        viewModelScope.launch {
            _drawingResultUiState.update {
                it.copy(
                    saveCompleteDialogVisible = state
                )
            }
        }
    }

    private fun getResultBitmap(
        recommendAndroidBitmap: Bitmap,
        outlineAndroidBitmap: Bitmap,
        latestBitmap: Bitmap,
    ): Bitmap {
        val resultBitmap = getEmptyBitmapBySize(
            recommendAndroidBitmap.width,
            recommendAndroidBitmap.height,
            recommendAndroidBitmap.density,
            whiteBackground = true
        )

        val resultCanvas = Canvas(resultBitmap)
        val paint = Paint()

        resultCanvas.drawBitmap(recommendAndroidBitmap, 0f, 0f, paint)
        resultCanvas.drawBitmap(latestBitmap, 0f, 0f, paint)
        resultCanvas.drawBitmap(outlineAndroidBitmap, 0f, 0f, paint)

        return resultBitmap
    }
}