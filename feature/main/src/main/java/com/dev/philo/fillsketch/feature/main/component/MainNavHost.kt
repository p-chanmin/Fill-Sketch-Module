package com.dev.philo.fillsketch.feature.main.component

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.drawing.navigation.drawingNavGraph
import com.dev.philo.fillsketch.feature.home.navigation.homeNavGraph
import com.dev.philo.fillsketch.feature.main.AdMobManager
import com.dev.philo.fillsketch.feature.main.MainNavigator

@Composable
internal fun MainNavHost(
    navigator: MainNavigator,
    padding: PaddingValues,
    playSoundEffect: (SoundEffect) -> Unit = {},
    onShowErrorSnackBar: (String) -> Unit,
    adMobManager: AdMobManager,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        homeNavGraph(
            paddingValues = padding,
            playSoundEffect = playSoundEffect,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = { navigator.popBackStackIfNotHome() },
            navigateToSketchList = { navigator.navigateToSketchList() },
            navigateToMyWorks = { navigator.navigateToMyWorks() },
            navigateToDrawing = { sketchType, drawingResultId ->
                navigator.navigateToDrawing(
                    sketchType,
                    drawingResultId
                )
            },
            navigateToDrawingResult = { sketchType, drawingResultId ->
                navigator.navigateToDrawingResult(
                    sketchType,
                    drawingResultId
                )
            },
            showSketchRewardAd = adMobManager::showSketchRewardAd
        )

        drawingNavGraph(
            paddingValues = padding,
            playSoundEffect = playSoundEffect,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = { navigator.popBackStackIfNotHome() },
            navigateToDrawing = { sketchType, drawingResultId ->
                navigator.navigateToDrawing(
                    sketchType,
                    drawingResultId
                )
            },
            navigateToDrawingResult = { sketchType, drawingResultId ->
                navigator.navigateToDrawingResult(
                    sketchType,
                    drawingResultId
                )
            },
            navigateToMyWorks = { navigator.navigateToMyWorks() },
            showMagicRewardAd = adMobManager::showMagicRewardAd,
            showInterstitialRewardAd = adMobManager::showInterstitialRewardAd
        )
    }
}