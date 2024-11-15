package com.dev.philo.fillsketch.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.core.navigation.Route
import com.dev.philo.fillsketch.feature.home.screen.HomeScreen
import com.dev.philo.fillsketch.feature.home.screen.MyWorksScreen
import com.dev.philo.fillsketch.feature.home.screen.SketchListScreen

fun NavController.navigateToSketchList() {
    navigate(Route.SketchList)
}

fun NavController.navigateToMyWorks() {
    navigate(Route.MyWorks)
}

fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues,
    playSoundEffect: (SoundEffect) -> Unit = {},
    onShowErrorSnackBar: (String) -> Unit,
    onBackClick: () -> Unit,
    navigateToSketchList: () -> Unit,
    navigateToMyWorks: () -> Unit,
    navigateToDrawing: (Int, Long) -> Unit,
    navigateToDrawingResult: (Int, Long) -> Unit,
    showSketchRewardAd: (() -> Unit, () -> Unit) -> Unit,
) {
    composable<Route.Home> {
        HomeScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            navigateToSketchList = navigateToSketchList,
            navigateToMyWorks = navigateToMyWorks,
            playSoundEffect = playSoundEffect,
        )
    }

    composable<Route.SketchList> {
        SketchListScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawing = navigateToDrawing,
            playSoundEffect = playSoundEffect,
            showSketchRewardAd = showSketchRewardAd,
        )
    }

    composable<Route.MyWorks> {
        MyWorksScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawingResult = navigateToDrawingResult,
            playSoundEffect = playSoundEffect,
        )
    }
}