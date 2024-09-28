package com.dev.philo.fillsketch.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.philo.fillsketch.core.data.repository.SettingRepository
import com.dev.philo.fillsketch.core.model.Setting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingRepository: SettingRepository
): ViewModel() {

    private val _setting = MutableStateFlow(Setting())
    val setting = _setting.asStateFlow()

    init {
        settingRepository.getSettings().onEach {
            _setting.value = it
        }.launchIn(viewModelScope)
    }
}