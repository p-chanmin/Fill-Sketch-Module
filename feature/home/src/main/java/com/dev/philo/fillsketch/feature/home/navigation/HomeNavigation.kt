package com.dev.philo.fillsketch.feature.home.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
    navigateToDrawing: () -> Unit,
    navigateToDrawingResult: () -> Unit
) {
    composable<Route.Home>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        HomeScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            navigateToSketchList = navigateToSketchList,
            navigateToMyWorks = navigateToMyWorks
        )
    }

    composable<Route.SketchList>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        SketchListScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawing = navigateToDrawing,
        )
    }

    composable<Route.MyWorks>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        MyWorksScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawingResult = navigateToDrawingResult
        )
    }
}