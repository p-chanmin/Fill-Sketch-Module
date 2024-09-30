package com.dev.philo.fillsketch.core.model

data class Setting(
    val soundEffect: Boolean = false,
    val backgroundMusic: Boolean = false
)

enum class SoundEffect {
    BUTTON_CLICK,
    SELECT_DRAWING_ACTION_TYPE
}