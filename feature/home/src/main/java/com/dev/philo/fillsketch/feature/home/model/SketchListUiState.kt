package com.dev.philo.fillsketch.feature.home.model

import androidx.compose.runtime.Immutable
import com.dev.philo.fillsketch.core.model.Sketch
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class SketchListUiState(
    val sketchList: ImmutableList<Sketch> = persistentListOf(),
    val selectedSketchId: Int? = null,
    val myWorks: ImmutableList<MyWork> = persistentListOf(),
    val dialogMyWorksVisible: Boolean = false,
    val dialogUnlockVisible: Boolean = false,
)