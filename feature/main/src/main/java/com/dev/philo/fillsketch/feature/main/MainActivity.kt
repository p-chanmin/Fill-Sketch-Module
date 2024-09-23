package com.dev.philo.fillsketch.feature.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navigator: MainNavigator = rememberMainNavigator()

            FillSketchTheme {
                MainScreen(
                    navigator = navigator
                )
            }
        }
    }
}
