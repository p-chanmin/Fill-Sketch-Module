package com.dev.philo.fillsketch.core.data.repository

import com.dev.philo.fillsketch.core.database.datasource.FillSketchDataSource
import com.dev.philo.fillsketch.core.model.Setting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val fillSketchDataSource: FillSketchDataSource
) {
    fun getSettings(): Flow<Setting> = fillSketchDataSource.settings
        .map {
            Setting(
                soundEffect = it.soundEffect,
                backgroundMusic = it.backgroundMusic
            )
        }

    suspend fun updateBackgroundMusicSetting() {
        fillSketchDataSource.updateBackgroundMusicSetting()
    }

    suspend fun updateSoundEffectSetting() {
        fillSketchDataSource.updateSoundEffectSetting()
    }
}