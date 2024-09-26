package com.dev.philo.fillsketch.feature.home.viewmodel

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
        return sketchRepository.getMyWorkBySketchType(sketchType).first()
            .map { drawingResult ->
                MyWork.create(drawingResult)
            }.toPersistentList()
    }

    fun selectSketch(sketchType: Int) {
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
                        addMyWork(sketchType)
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

            } else {
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
    }

    fun addMyWork(sketchType: Int) {
        viewModelScope.launch {
            _uiEventFlow.emit(
                SketchListUiEvent.NavigateToDrawing(
                    sketchType,
                    sketchRepository.addMyWork(sketchType)
                )
            )
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

    fun deleteMyWork(sketchType: Int, drawingResultId: Int) {
        viewModelScope.launch {
            sketchRepository.deleteMyWork(drawingResultId)
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
}