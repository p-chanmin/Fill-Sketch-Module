package com.dev.philo.fillsketch.feature.home.model

import androidx.compose.runtime.Immutable
import com.dev.philo.fillsketch.core.model.Setting

@Immutable
data class HomeUiState(
    val setting: Setting = Setting()
)
