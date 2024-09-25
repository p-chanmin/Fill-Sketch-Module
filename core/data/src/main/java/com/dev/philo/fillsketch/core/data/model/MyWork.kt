package com.dev.philo.fillsketch.core.data.model

data class MyWork (
    var id: Int = 0,
    var sketchType: Int = 0,
    var paths: List<PathWrapper> = listOf()
)