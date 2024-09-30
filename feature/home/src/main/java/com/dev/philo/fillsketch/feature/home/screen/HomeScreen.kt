package com.dev.philo.fillsketch.feature.home.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchMainButton
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.home.R
import com.dev.philo.fillsketch.feature.home.model.HomeUiState
import com.dev.philo.fillsketch.feature.home.viewmodel.HomeViewModel
import kotlin.math.roundToInt
import com.dev.philo.fillsketch.asset.R as AssetR
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    navigateToSketchList: () -> Unit,
    navigateToMyWorks: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()

    HomeContent(
        homeUiState = homeUiState,
        navigateToSketchList = navigateToSketchList,
        navigateToMyWorks = navigateToMyWorks,
        updateSoundEffectSetting = homeViewModel::updateSoundEffectSetting,
        updateBackgroundMusicSetting = homeViewModel::updateBackgroundMusicSetting,
        playSoundEffect = playSoundEffect,
    )
}

@Composable
fun HomeContent(
    homeUiState: HomeUiState,
    navigateToSketchList: () -> Unit,
    navigateToMyWorks: () -> Unit,
    updateSoundEffectSetting: () -> Unit,
    updateBackgroundMusicSetting: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
) {
    val context = LocalContext.current
    var settingDialogVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(
                id = com.dev.philo.fillsketch.asset.R.drawable.img_background
            ),
            contentDescription = stringResource(R.string.feature_home_background),
            contentScale = ContentScale.Crop
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = Paddings.xlarge, end = Paddings.xlarge),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FillSketchSettingButton(
                modifier = Modifier.size(60.dp),
                painter = if (homeUiState.setting.soundEffect) {
                    painterResource(id = DesignSystemR.drawable.ic_se_on)
                } else {
                    painterResource(id = DesignSystemR.drawable.ic_se_off)
                },
                contentDescription = stringResource(R.string.feature_home_sound_setting),
                onClick = { updateSoundEffectSetting() }
            )

            FillSketchSettingButton(
                modifier = Modifier.size(60.dp),
                playSoundEffect = playSoundEffect,
                painter = if (homeUiState.setting.backgroundMusic) {
                    painterResource(id = DesignSystemR.drawable.ic_bgm_on)
                } else {
                    painterResource(id = DesignSystemR.drawable.ic_bgm_off)
                },
                contentDescription = stringResource(R.string.feature_home_bgm_setting),
                onClick = { updateBackgroundMusicSetting() }
            )

            FillSketchSettingButton(
                modifier = Modifier.size(60.dp),
                playSoundEffect = playSoundEffect,
                painter = painterResource(id = DesignSystemR.drawable.ic_setting),
                contentDescription = stringResource(R.string.feature_home_setting),
                onClick = { settingDialogVisible = true }
            )
        }

        val titleOffset = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                titleOffset.animateTo(
                    targetValue = 20f,
                    animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
                )
                titleOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 120.dp)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .rotate(-3f)
                    .offset { IntOffset(0, titleOffset.value.roundToInt()) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedText(
                    textModifier = Modifier
                        .padding(bottom = Paddings.xsmall),
                    text = stringResource(id = AssetR.string.asset_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 60.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    outlineColor = MaterialTheme.colorScheme.onTertiary,
                    outlineDrawStyle = Stroke(
                        width = 15f
                    )
                )

                OutlinedText(
                    textModifier = Modifier
                        .padding(bottom = Paddings.xsmall),
                    text = stringResource(id = AssetR.string.asset_version_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    outlineColor = MaterialTheme.colorScheme.onTertiary,
                    outlineDrawStyle = Stroke(
                        width = 15f
                    )
                )
            }
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp),
                painter = painterResource(
                    id = AssetR.drawable.img_home_character
                ),
                contentDescription = stringResource(R.string.feature_home_background_character),
                contentScale = ContentScale.FillHeight
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = Paddings.large),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            FillSketchMainButton(
                modifier = Modifier.size(100.dp),
                playSoundEffect = playSoundEffect,
                painter = painterResource(id = AssetR.drawable.img_sketch),
                badge = painterResource(id = DesignSystemR.drawable.ic_playstore),
                onClick = {}
            )

            FillSketchMainButton(
                modifier = Modifier.size(100.dp),
                playSoundEffect = playSoundEffect,
                painter = painterResource(id = AssetR.drawable.img_sketch),
                badge = painterResource(id = DesignSystemR.drawable.ic_playstore),
                onClick = {}
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Paddings.xlarge + 60.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                FillSketchMainButton(
                    modifier = Modifier.size(150.dp),
                    playSoundEffect = playSoundEffect,
                    text = stringResource(R.string.feature_home_sketch),
                    painter = painterResource(id = AssetR.drawable.img_sketch),
                    onClick = { navigateToSketchList() }
                )

                Spacer(modifier = Modifier.size(24.dp))

                FillSketchMainButton(
                    modifier = Modifier.size(150.dp),
                    playSoundEffect = playSoundEffect,
                    text = stringResource(R.string.feature_home_myworks),
                    painter = painterResource(id = AssetR.drawable.img_myworks),
                    onClick = { navigateToMyWorks() }
                )
            }
        }
    }

    if (settingDialogVisible) {
        SettingDialog(
            onDismissRequest = { settingDialogVisible = false },
            playSoundEffect = playSoundEffect,
            onShareClick = {
                val appPackageName = context.packageName
                val uri = context.getString(
                    R.string.feature_home_play_store_uri,
                    appPackageName
                )
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        context.getString(
                            R.string.feature_home_share_description,
                            uri
                        )
                    )
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share"))
            },
            onRateClick = {
                val appPackageName = context.packageName
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            context.getString(
                                R.string.feature_home_play_store_uri,
                                appPackageName
                            )
                        )
                    )
                )
            },
            onPrivacyPolicyClick = {
                val url = context.getString(R.string.feature_home_privacy_policy_uri)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            onTermsOfServiceClick = {
                val url = context.getString(R.string.feature_home_terms_of_service_uri)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            onSourceSupportClick = {
                val url = context.getString(R.string.feature_home_source_support_uri)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
        )
    }
}

@Composable
fun SettingDialog(
    onDismissRequest: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
    onShareClick: () -> Unit,
    onRateClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onSourceSupportClick: () -> Unit,
) {
    FillSketchDialog(
        onDismissRequest = { onDismissRequest() },
        playSoundEffect = playSoundEffect
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Paddings.small)
                    .clickable {
                        playSoundEffect(SoundEffect.BUTTON_CLICK)
                        onShareClick()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = Paddings.large)
                        .size(32.dp),
                    painter = painterResource(id = DesignSystemR.drawable.ic_share),
                    contentDescription = stringResource(R.string.feature_home_share)
                )
                OutlinedText(
                    modifier = Modifier.padding(start = Paddings.large),
                    text = stringResource(R.string.feature_home_share),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    ),
                    outlineColor = MaterialTheme.colorScheme.onTertiary,
                    outlineDrawStyle = Stroke(
                        width = 5f
                    )
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Paddings.large),
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Paddings.small)
                    .clickable {
                        playSoundEffect(SoundEffect.BUTTON_CLICK)
                        onRateClick()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .padding(start = Paddings.large)
                        .size(32.dp),
                    painter = painterResource(id = DesignSystemR.drawable.ic_rate),
                    contentDescription = stringResource(R.string.feature_home_rate_us)
                )
                OutlinedText(
                    modifier = Modifier.padding(start = Paddings.large),
                    text = stringResource(R.string.feature_home_rate_us),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    ),
                    outlineColor = MaterialTheme.colorScheme.onTertiary,
                    outlineDrawStyle = Stroke(
                        width = 5f
                    )
                )
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = Paddings.xextra, bottom = Paddings.medium),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            playSoundEffect(SoundEffect.BUTTON_CLICK)
                            onPrivacyPolicyClick()
                        },
                        text = stringResource(R.string.feature_home_privacy_policy),
                        style = TextStyle(
                            fontFamily = null,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        textDecoration = TextDecoration.Underline
                    )

                    Text(
                        modifier = Modifier.clickable {
                            playSoundEffect(SoundEffect.BUTTON_CLICK)
                            onTermsOfServiceClick()
                        },
                        text = stringResource(R.string.feature_home_terms_of_service),
                        style = TextStyle(
                            fontFamily = null,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        textDecoration = TextDecoration.Underline
                    )

                    Text(
                        modifier = Modifier.clickable {
                            playSoundEffect(SoundEffect.BUTTON_CLICK)
                            onSourceSupportClick()
                        },
                        text = stringResource(R.string.feature_home_source_support),
                        style = TextStyle(
                            fontFamily = null,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Preview(device = "id:pixel_c")
@Composable
fun HomeContentPreview() {
    FillSketchTheme {
        HomeContent(
            homeUiState = HomeUiState(),
            navigateToSketchList = {},
            navigateToMyWorks = {},
            updateSoundEffectSetting = {},
            updateBackgroundMusicSetting = {},
        )
    }
}