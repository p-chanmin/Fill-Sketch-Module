package com.dev.philo.fillsketch.feature.home.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SketchListUiState(
    val sketchList: ImmutableList<Int> = persistentListOf()
)
