package com.dev.philo.fillsketch.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
    onShowErrorSnackBar: (String) -> Unit,
    onBackClick: () -> Unit,
    navigateToSketchList: () -> Unit,
    navigateToMyWorks: () -> Unit,
    navigateToDrawing: (Int, Int) -> Unit,
    navigateToDrawingResult: (Int, Int) -> Unit
) {
    composable<Route.Home> {
        HomeScreen(
            paddingValues = paddingValues,
            playButtonSound = playButtonSound,
            onShowErrorSnackBar = onShowErrorSnackBar,
            navigateToSketchList = navigateToSketchList,
            navigateToMyWorks = navigateToMyWorks
        )
    }

    composable<Route.SketchList> {
        SketchListScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawing = navigateToDrawing,
        )
    }

    composable<Route.MyWorks> {
        MyWorksScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawingResult = navigateToDrawingResult
        )
    }
}