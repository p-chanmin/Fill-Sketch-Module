package com.dev.philo.fillsketch.feature.home.model

import androidx.compose.runtime.Immutable
import com.dev.philo.fillsketch.core.data.model.Setting
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HomeUiState(
    val setting: Setting = Setting()
)
