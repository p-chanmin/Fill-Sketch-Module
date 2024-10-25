package com.dev.philo.fillsketch.feature.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.main.component.MainNavHost
import com.dev.philo.fillsketch.feature.main.manager.AdMobManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    playSoundEffect: (SoundEffect) -> Unit = {},
    adMobManager: AdMobManager,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()
    val onShowErrorSnackBar: (String) -> Unit = { message ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    MainScreenContent(
        navigator = navigator,
        playSoundEffect = playSoundEffect,
        onShowErrorSnackBar = onShowErrorSnackBar,
        snackBarHostState = snackBarHostState,
        adMobManager = adMobManager,
    )
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    playSoundEffect: (SoundEffect) -> Unit = {},
    onShowErrorSnackBar: (String) -> Unit,
    snackBarHostState: SnackbarHostState,
    adMobManager: AdMobManager,
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                padding = padding,
                playSoundEffect = playSoundEffect,
                onShowErrorSnackBar = onShowErrorSnackBar,
                adMobManager = adMobManager,
            )
        },
        bottomBar = {
            BannersAds()
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}

@Composable
fun BannersAds(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.FULL_BANNER)
                adUnitId = BuildConfig.ADMOB_BANNER_ID
            }
        },
        update = { adView ->
            adView.loadAd(AdRequest.Builder().build())
        }
    )
}