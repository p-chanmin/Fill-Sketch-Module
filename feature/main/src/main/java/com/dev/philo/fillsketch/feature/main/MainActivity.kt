package com.dev.philo.fillsketch.feature.main

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.model.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import com.dev.philo.fillsketch.asset.R as AssetR

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var bgmMediaPlayer: MediaPlayer? = null
    private lateinit var soundPool: SoundPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
            hide(WindowInsetsCompat.Type.navigationBars())
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        val soundEffectManager = SoundEffectManager(this, soundPool)

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()
            val settings by viewModel.setting.collectAsStateWithLifecycle()

            if (settings.backgroundMusic) {
                if (bgmMediaPlayer == null) {
                    bgmMediaPlayer = MediaPlayer.create(this@MainActivity, AssetR.raw.bgm_default)
                    bgmMediaPlayer?.isLooping = true
                    bgmMediaPlayer?.start()
                }
            } else {
                bgmMediaPlayer?.stop()
                bgmMediaPlayer?.release()
                bgmMediaPlayer = null
            }

            val playSoundEffect: (SoundEffect) -> Unit = { soundEffect ->
                if (settings.soundEffect) {
                    soundEffectManager.playSoundEffect(soundEffect)
                }
            }

            FillSketchTheme {
                MainScreen(
                    navigator = navigator,
                    playSoundEffect = playSoundEffect
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        bgmMediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.setting.value.backgroundMusic) {
            bgmMediaPlayer?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bgmMediaPlayer?.release()
        soundPool.release()
    }
}
