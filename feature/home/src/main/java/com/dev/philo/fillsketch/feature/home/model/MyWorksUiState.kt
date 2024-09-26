package com.dev.philo.fillsketch.feature.home.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyWorksUiState(
    val selectedMyWorkId: Int? = null,
    val myWorks: ImmutableList<MyWork> = persistentListOf(),
    val dialogMyWorksDeleteVisible: Boolean = false,
)