package com.dev.philo.fillsketch.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.SketchRepository
import com.dev.philo.fillsketch.feature.home.model.SketchListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        sketchRepository.getSketch().onEach { sketchList ->
            _sketchListUiState.update {
                it.copy(
                    sketchList = sketchList.toPersistentList()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun selectSketch(sketchType: Int) {
        viewModelScope.launch {
            if (_sketchListUiState.value.selectedSketchId == null) {
                _sketchListUiState.update {
                    if (_sketchListUiState.value.sketchList[sketchType].isLocked) {
                        it.copy(
                            selectedSketchId = sketchType,
                            dialogUnlockVisible = true
                        )
                    } else {
                        it.copy(
                            selectedSketchId = sketchType,
                            myWorks = sketchRepository.getMyWorkBySketchType(sketchType).first()
                                .toPersistentList(),
                            dialogMyWorksVisible = true
                        )
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

    fun unlockSketch(sketchType: Int) {
        viewModelScope.launch {
            sketchRepository.updateLockState(sketchType, isLocked = false)
        }
    }
}