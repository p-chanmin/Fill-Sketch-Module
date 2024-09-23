package com.dev.philo.fillsketch.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object SketchList : Route

    @Serializable
    data object MyWorks : Route

    @Serializable
    data object Drawing : Route

    @Serializable
    data object DrawingResult : Route
}