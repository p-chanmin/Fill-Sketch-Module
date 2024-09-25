package com.dev.philo.fillsketch.feature.home.model

import com.dev.philo.fillsketch.core.data.model.Setting
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeUiState(
    val setting: Setting = Setting()
)
