package com.dev.philo.fillsketch.feature.main.component

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.dev.philo.fillsketch.feature.home.navigation.drawingNavGraph
import com.dev.philo.fillsketch.feature.home.navigation.homeNavGraph
import com.dev.philo.fillsketch.feature.main.MainNavigator

@Composable
internal fun MainNavHost(
    navigator: MainNavigator,
    padding: PaddingValues,
    onShowErrorSnackBar: (String) -> Unit,
) {
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        homeNavGraph(
            paddingValues = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = { navigator.popBackStackIfNotHome() },
            navigateToSketchList = { navigator.navigateToSketchList() },
            navigateToMyWorks = { navigator.navigateToMyWorks() },
            navigateToDrawing = { navigator.navigateToDrawing() },
            navigateToDrawingResult = { navigator.navigateToDrawingResult() }
        )

        drawingNavGraph(
            paddingValues = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onBackClick = { navigator.popBackStackIfNotHome() },
            navigateToDrawing = { navigator.navigateToDrawing() },
            navigateToDrawingResult = { navigator.navigateToDrawingResult() },
            navigateToMyWorks = { navigator.navigateToMyWorks() }
        )
    }
}