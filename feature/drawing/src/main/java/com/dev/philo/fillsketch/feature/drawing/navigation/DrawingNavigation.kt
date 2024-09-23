package com.dev.philo.fillsketch.feature.home.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dev.philo.fillsketch.core.navigation.Route
import com.dev.philo.fillsketch.feature.drawing.screen.DrawingResultScreen
import com.dev.philo.fillsketch.feature.drawing.screen.DrawingScreen

fun NavController.navigateToDrawing() {
    navigate(Route.Drawing)
}

fun NavController.navigateToDrawingResult() {
    navigate(Route.DrawingResult)
}

fun NavGraphBuilder.drawingNavGraph(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: () -> Unit,
    navigateToDrawingResult: () -> Unit,
    navigateToMyWorks: () -> Unit
) {
    composable<Route.Drawing>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        DrawingScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawingResult = navigateToDrawingResult
        )
    }

    composable<Route.DrawingResult>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        DrawingResultScreen(
            paddingValues = paddingValues,
            onShowErrorSnackBar = onShowErrorSnackBar,
            navigateToDrawing = navigateToDrawing,
            navigateToMyWorks = navigateToMyWorks
        )
    }
}