package com.dev.philo.fillsketch.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.feature.home.model.SketchListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SketchListViewModel @Inject constructor(): ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()

    private val _sketchListUiState = MutableStateFlow(SketchListUiState())
    val sketchListUiState = _sketchListUiState.asStateFlow()

    init {
        _sketchListUiState.update {
            it.copy(sketchList = SketchResource.sketchOutlineResourceIds.toPersistentList())
        }
    }
}