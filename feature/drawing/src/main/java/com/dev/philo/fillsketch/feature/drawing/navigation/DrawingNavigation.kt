package com.dev.philo.fillsketch.feature.drawing.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dev.philo.fillsketch.core.navigation.Route
import com.dev.philo.fillsketch.feature.drawing.screen.DrawingResultScreen
import com.dev.philo.fillsketch.feature.drawing.screen.DrawingScreen

fun NavController.navigateToDrawing(sketchType: Int, drawingResultId: Int) {
    navigate(Route.Drawing(sketchType, drawingResultId))
}

fun NavController.navigateToDrawingResult(sketchType: Int, drawingResultId: Int) {
    navigate(Route.DrawingResult(sketchType, drawingResultId))
}

fun NavGraphBuilder.drawingNavGraph(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: (Int, Int) -> Unit,
    navigateToDrawingResult: (Int, Int) -> Unit,
    navigateToMyWorks: () -> Unit
) {
    composable<Route.Drawing> { navBackStackEntry ->
        val (sketchType, drawingResultId) = navBackStackEntry.toRoute<Route.Drawing>()
        DrawingScreen(
            paddingValues = paddingValues,
            sketchType = sketchType,
            drawingResultId = drawingResultId,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawingResult = navigateToDrawingResult
        )
    }

    composable<Route.DrawingResult> { navBackStackEntry ->
        val (sketchType, drawingResultId) = navBackStackEntry.toRoute<Route.Drawing>()
        DrawingResultScreen(
            paddingValues = paddingValues,
            sketchType = sketchType,
            drawingResultId = drawingResultId,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = onBackClick,
            navigateToDrawing = navigateToDrawing,
            navigateToMyWorks = navigateToMyWorks
        )
    }
}