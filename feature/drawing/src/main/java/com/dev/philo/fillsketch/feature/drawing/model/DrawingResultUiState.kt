package com.dev.philo.fillsketch.feature.drawing.model

import androidx.compose.runtime.Immutable
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class DrawingResultUiState(
    val paths: ImmutableList<PathWrapper> = persistentListOf(),
    val saveCompleteDialogVisible: Boolean = false,
)
