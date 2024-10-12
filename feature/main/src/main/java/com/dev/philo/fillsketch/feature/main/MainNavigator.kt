package com.dev.philo.fillsketch.feature.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dev.philo.fillsketch.core.navigation.Route
import com.dev.philo.fillsketch.feature.drawing.navigation.navigateToDrawing
import com.dev.philo.fillsketch.feature.drawing.navigation.navigateToDrawingResult
import com.dev.philo.fillsketch.feature.home.navigation.navigateToMyWorks
import com.dev.philo.fillsketch.feature.home.navigation.navigateToSketchList

internal class MainNavigator(
    val navController: NavHostController,
) {
    val startDestination = Route.Home

    fun navigateToSketchList() {
        navController.navigateToSketchList()
    }

    fun navigateToMyWorks() {
        navController.popBackStack<Route.Home>(inclusive = false)
        navController.navigateToMyWorks()
    }

    fun navigateToDrawing(sketchType: Int, drawingResultId: Long) {
        navController.popBackStack<Route.Drawing>(inclusive = true)
        navController.popBackStack<Route.DrawingResult>(inclusive = true)
        navController.navigateToDrawing(sketchType, drawingResultId)
    }

    fun navigateToDrawingResult(sketchType: Int, drawingResultId: Long) {
        navController.navigateToDrawingResult(sketchType, drawingResultId)
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotHome() {
        if (!isSameCurrentDestination<Route.Home>()) {
            popBackStack()
        }
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
