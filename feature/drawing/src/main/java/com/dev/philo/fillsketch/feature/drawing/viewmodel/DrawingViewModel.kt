package com.dev.philo.fillsketch.feature.drawing.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.DrawingResultRepository
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper.Companion.toPathData
import com.dev.philo.fillsketch.core.designsystem.utils.createPath
import com.dev.philo.fillsketch.core.designsystem.utils.getEmptyBitmapBySize
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.model.DrawingUiState
import com.dev.philo.fillsketch.feature.drawing.model.MyWork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val drawingResultRepository: DrawingResultRepository
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()

    private val _drawingUiState = MutableStateFlow(DrawingUiState())
    val drawingUiState = _drawingUiState.asStateFlow()

    private val _redoPathList = mutableStateListOf<PathWrapper>()
    private val _undoPathList = mutableStateListOf<PathWrapper>()
    val pathList: SnapshotStateList<PathWrapper> = _undoPathList

    val redoPathSize get() = _redoPathList.size
    val undoPathSize get() = _undoPathList.size

    val currentMaskBitmap = mutableStateOf(
        getEmptyBitmapBySize(1, 1, 160, whiteBackground = true)
    )

    val liveDrawingMaskBitmap = mutableStateOf(
        getEmptyBitmapBySize(1, 1, 160, whiteBackground = false)
    )

    fun fetchDrawingUiState(
        sketchType: Int,
        drawingResultId: Int,
        width: Int,
        height: Int,
        dpi: Int
    ) {
        viewModelScope.launch {
            val myWork =
                MyWork.create(
                    drawingResultRepository.getDrawingResult(sketchType, drawingResultId).first()
                )
            _undoPathList.addAll(myWork.paths)
            currentMaskBitmap.value =
                getEmptyBitmapBySize(width, height, dpi, whiteBackground = true)
            liveDrawingMaskBitmap.value =
                getEmptyBitmapBySize(width, height, dpi, whiteBackground = false)
            _drawingUiState.update {
                it.copy(
                    drawingResultId = myWork.id,
                    sketchType = myWork.sketchType,
                    width = width,
                    height = height,
                    dpi = dpi,
                    hasMagicBrush = myWork.hasMagicBrush
                )
            }
            drawOnNewMask()
        }
        drawingResultRepository.getMagicBrushState(sketchType).onEach { hasMagicBrush ->
            _drawingUiState.update {
                it.copy(
                    hasMagicBrush = hasMagicBrush
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateMagicBrushState() {
        viewModelScope.launch {
            drawingResultRepository.updateMagicBrushState(_drawingUiState.value.sketchType, true)
        }
    }

    fun updateColor(value: Color) {
        _drawingUiState.update {
            it.copy(
                strokeColor = value
            )
        }
    }

    fun updateStrokeWidth(value: Float) {
        _drawingUiState.update {
            it.copy(
                strokeWidth = value
            )
        }
    }

    fun updateActionType(value: ActionType) {
        _drawingUiState.update {
            it.copy(
                actionType = value
            )
        }
    }

    fun undo() {
        if (_undoPathList.isNotEmpty()) {
            val last = _undoPathList.last()
            _redoPathList.add(last)
            _undoPathList.remove(last)
            drawOnNewMask()
        }
    }

    fun redo() {
        if (_redoPathList.isNotEmpty()) {
            val last = _redoPathList.last()
            _undoPathList.add(last)
            _redoPathList.remove(last)
            drawOnNewMask()
        }
    }


    fun reset() {
        _redoPathList.clear()
        _undoPathList.clear()
        drawOnNewMask()
    }

    fun insertNewPath(newPoint: Offset) {
        val pathWrapper = PathWrapper(
            points = mutableStateListOf(newPoint),
            strokeColor = _drawingUiState.value.strokeColor,
            actionType = _drawingUiState.value.actionType,
            strokeWidth = _drawingUiState.value.strokeWidth,
        )
        _undoPathList.add(pathWrapper)
        _redoPathList.clear()
        drawOnLivePath()
    }

    fun updateLatestPath(newPoint: Offset) {
        val index = _undoPathList.lastIndex
        _undoPathList[index].points.add(newPoint)
        drawOnLivePath()
    }

    fun drawOnNewMask() {
        val maskBitmap = getEmptyBitmapBySize(
            _drawingUiState.value.width,
            _drawingUiState.value.height,
            _drawingUiState.value.dpi,
            whiteBackground = true
        )

        val canvas = Canvas(maskBitmap)

        pathList.forEach { path ->
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

            val pathObj = createPath(path.points)

            canvas.drawPath(pathObj, paint)
        }
        currentMaskBitmap.value = maskBitmap
        liveDrawingMaskBitmap.value = getEmptyBitmapBySize(
            _drawingUiState.value.width,
            _drawingUiState.value.height,
            _drawingUiState.value.dpi,
            whiteBackground = false
        )

        viewModelScope.launch {
            saveCurrentDrawingResult()
        }
    }

    private fun drawOnLivePath() {
        pathList.lastOrNull()?.let { path ->

            val maskBitmap = if (path.actionType == ActionType.MAGIC_BRUSH) {
                currentMaskBitmap.value.copy(Bitmap.Config.ARGB_8888, true)
            } else {
                getEmptyBitmapBySize(
                    _drawingUiState.value.width,
                    _drawingUiState.value.height,
                    _drawingUiState.value.dpi,
                    whiteBackground = false
                )
            }

            val canvas = Canvas(maskBitmap)

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
            val pathObj = createPath(path.points)
            canvas.drawPath(pathObj, paint)

            if (path.actionType == ActionType.MAGIC_BRUSH) {
                currentMaskBitmap.value = maskBitmap
            } else {
                liveDrawingMaskBitmap.value = maskBitmap
            }
        }
    }

    private suspend fun saveCurrentDrawingResult() {
        drawingResultRepository.updateDrawingResult(
            _drawingUiState.value.drawingResultId,
            _undoPathList.map { it.toPathData() }
        )
    }
}