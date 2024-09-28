package com.dev.philo.fillsketch.feature.main

import android.content.Context
import android.media.SoundPool
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

class SoundEffectManager(private val context: Context, private val soundPool: SoundPool) {
    private val soundMap = mutableMapOf<SoundEffect, Int>()

    init {
        SoundEffect.entries.forEach { se ->
            val resourceId = when (se) {
                SoundEffect.BUTTON_CLICK -> DesignSystemR.raw.se_button_click
                SoundEffect.DRAWING -> DesignSystemR.raw.se_drawing
                SoundEffect.SELECT_DRAWING_ACTION_TYPE -> DesignSystemR.raw.se_select_drawing_action_type
            }
            val soundId = soundPool.load(context, resourceId, 1)
            soundMap[se] = soundId
        }
    }

    fun playSoundEffect(soundEffect: SoundEffect) {
        soundMap[soundEffect]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }
}