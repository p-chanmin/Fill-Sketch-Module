package com.dev.philo.fillsketch.feature.main

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.main.component.MainNavHost
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    playSoundEffect: (SoundEffect) -> Unit = {},
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()
    val onShowErrorSnackBar: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(message)
        }
    }

    MainScreenContent(
        navigator = navigator,
        playSoundEffect = playSoundEffect,
        onShowErrorSnackBar = onShowErrorSnackBar,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    playSoundEffect: (SoundEffect) -> Unit = {},
    onShowErrorSnackBar: (String) -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                padding = padding,
                playSoundEffect = playSoundEffect,
                onShowErrorSnackBar = onShowErrorSnackBar,
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}