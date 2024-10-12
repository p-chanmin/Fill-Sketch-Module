package com.dev.philo.fillsketch.feature.home.viewmodel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.SketchRepository
import com.dev.philo.fillsketch.feature.home.model.MyWork
import com.dev.philo.fillsketch.feature.home.model.SketchListUiEvent
import com.dev.philo.fillsketch.feature.home.model.SketchListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SketchListViewModel @Inject constructor(
    private val sketchRepository: SketchRepository
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()

    private val _sketchListUiState = MutableStateFlow(SketchListUiState())
    val sketchListUiState = _sketchListUiState.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<SketchListUiEvent>()
    val uiEventFlow get() = _uiEventFlow.asSharedFlow()

    init {
        sketchRepository.getSketch().onEach { sketchList ->
            _sketchListUiState.update {
                it.copy(
                    sketchList = sketchList.toPersistentList()
                )
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun getMyWorksBySketchType(sketchType: Int): PersistentList<MyWork> {
        return sketchRepository.getDrawingResultBySketchType(sketchType).first()
            .map { drawingResult ->
                MyWork.create(drawingResult)
            }.toPersistentList()
    }

    fun selectSketch(sketchType: Int, width: Int, height: Int, dpi: Int) {
        viewModelScope.launch {
            if (_sketchListUiState.value.selectedSketchId == null) {
                if (_sketchListUiState.value.sketchList[sketchType].isLocked) {
                    _sketchListUiState.update {
                        it.copy(
                            selectedSketchId = sketchType,
                            dialogUnlockVisible = true
                        )
                    }
                } else {
                    val myWorks = getMyWorksBySketchType(sketchType)

                    if (myWorks.isEmpty()) {
                        addMyWork(sketchType, width, height, dpi)
                    } else {
                        _sketchListUiState.update {
                            it.copy(
                                selectedSketchId = sketchType,
                                myWorks = myWorks,
                                dialogMyWorksVisible = true
                            )
                        }
                    }
                }
            }
        }
    }

    fun dismissDialog() {
        viewModelScope.launch {
            _sketchListUiState.update {
                it.copy(
                    selectedSketchId = null,
                    myWorks = persistentListOf(),
                    dialogMyWorksVisible = false,
                    dialogUnlockVisible = false
                )
            }
        }
    }

    fun addMyWork(sketchType: Int, width: Int, height: Int, dpi: Int) {
        viewModelScope.launch {
            dismissDialog()
            _uiEventFlow.emit(
                SketchListUiEvent.NavigateToDrawing(
                    sketchType,
                    sketchRepository.addDrawingResult(
                        sketchType,
                        bitmapToByteArray(createEmptyBitmap(width, height, dpi))
                    )
                )
            )
        }
    }

    fun deleteMyWork(sketchType: Int, drawingResultId: Long) {
        viewModelScope.launch {
            sketchRepository.deleteDrawingResult(drawingResultId)
            _sketchListUiState.update {
                it.copy(
                    myWorks = getMyWorksBySketchType(sketchType),
                )
            }
        }
    }

    fun unlockSketch(sketchType: Int) {
        viewModelScope.launch {
            sketchRepository.updateLockState(sketchType, isLocked = false)
        }
    }

    private fun createEmptyBitmap(width: Int, height: Int, dpi: Int): Bitmap {
        return Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888,
        ).apply {
            density = dpi
            val canvas = Canvas(this)
            val paint = Paint().apply {
                color = android.graphics.Color.WHITE
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}