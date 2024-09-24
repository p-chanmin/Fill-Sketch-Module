package com.dev.philo.fillsketch.feature.home.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchMainButton
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import kotlin.math.roundToInt
import com.dev.philo.fillsketch.asset.R as AssetR
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    navigateToSketchList: () -> Unit,
    navigateToMyWorks: () -> Unit
) {
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
            contentDescription = "background",
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
                painter = painterResource(id = DesignSystemR.drawable.ic_se_on),
                onClick = {}
            )

            FillSketchSettingButton(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = DesignSystemR.drawable.ic_bgm_on),
                onClick = {}
            )

            FillSketchSettingButton(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = DesignSystemR.drawable.ic_setting),
                onClick = {}
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
            modifier = Modifier.align(Alignment.Center).padding(top = 120.dp).fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
                    .rotate(-3f).offset { IntOffset(0, titleOffset.value.roundToInt()) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedText(
                    modifier = Modifier
                        .padding(bottom = Paddings.xsmall),
                    text = stringResource(id = AssetR.string.title),
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
                    modifier = Modifier
                        .padding(bottom = Paddings.xsmall),
                    text = stringResource(id = AssetR.string.version_title),
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
                modifier = Modifier.fillMaxSize().padding(top = 100.dp),
                painter = painterResource(
                    id = AssetR.drawable.img_home_character
                ),
                contentDescription = "main Character",
                contentScale = ContentScale.FillHeight
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(bottom = Paddings.extra, start = Paddings.large),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                FillSketchMainButton(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = AssetR.drawable.img_sketch),
                    badge = painterResource(id = DesignSystemR.drawable.ic_playstore),
                    onClick = {}
                )

                FillSketchMainButton(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(id = AssetR.drawable.img_sketch),
                    badge = painterResource(id = DesignSystemR.drawable.ic_playstore),
                    onClick = {}
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Paddings.xlarge),
                horizontalArrangement = Arrangement.Center,
            ) {
                FillSketchMainButton(
                    modifier = Modifier.size(150.dp),
                    text = "Sketch",
                    painter = painterResource(id = AssetR.drawable.img_sketch),
                    onClick = { navigateToSketchList() }
                )

                Spacer(modifier = Modifier.size(24.dp))

                FillSketchMainButton(
                    modifier = Modifier.size(150.dp),
                    text = "MyWorks",
                    painter = painterResource(id = AssetR.drawable.img_myworks),
                    onClick = { navigateToMyWorks() }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Preview(device = "id:pixel_c")
@Composable
fun HomeScreenPhonePreview() {
    FillSketchTheme {
        HomeScreen(
            paddingValues = PaddingValues(),
            onShowErrorSnackBar = {},
            navigateToSketchList = {},
            navigateToMyWorks = {}
        )
    }
}