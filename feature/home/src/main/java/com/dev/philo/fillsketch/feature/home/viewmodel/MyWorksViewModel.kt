package com.dev.philo.fillsketch.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.SketchRepository
import com.dev.philo.fillsketch.feature.home.model.MyWork
import com.dev.philo.fillsketch.feature.home.model.MyWorksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWorksViewModel @Inject constructor(
    private val sketchRepository: SketchRepository
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()

    private val _myWorksUiState = MutableStateFlow(MyWorksUiState())
    val myWorksUiState = _myWorksUiState.asStateFlow()

    init {
        sketchRepository.getDrawingResult().onEach { drawingResultList ->
            _myWorksUiState.update {
                it.copy(
                    myWorks = drawingResultList.map { MyWork.create(it) }.toPersistentList()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun deleteMyWork(drawingResultId: Long) {
        viewModelScope.launch {
            sketchRepository.deleteDrawingResult(drawingResultId)
        }
    }
}