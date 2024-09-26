package com.dev.philo.fillsketch.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.SettingRepository
import com.dev.philo.fillsketch.feature.home.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
class HomeViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow get() = _messageFlow.asSharedFlow()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    init {
        settingRepository.getSettings().onEach { newSetting ->
            _homeUiState.update {
                it.copy(setting = newSetting)
            }
        }.launchIn(viewModelScope)
    }

    fun updateBackgroundMusicSetting() {
        viewModelScope.launch {
            settingRepository.updateBackgroundMusicSetting()
        }
    }

    fun updateSoundEffectSetting() {
        viewModelScope.launch {
            settingRepository.updateSoundEffectSetting()
        }
    }
}