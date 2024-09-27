package com.dev.philo.fillsketch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.core.designsystem.R
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.asset.R as AssetR

@Composable
fun FillSketchMainButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    painterDescription: String? = null,
    badge: Painter? = null,
    badgeDescription: String? = null,
    text: String = "",
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
            .padding(5.dp),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(4.dp),
        shadowElevation = 2.dp,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .clickable { onClick() }
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                painter = painter,
                contentDescription = painterDescription,
                contentScale = ContentScale.Crop
            )

            badge?.let {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopStart)
                        .rotate(-10f)
                        .padding(top = Paddings.small, start = Paddings.small),
                    painter = badge,
                    contentDescription = badgeDescription,
                    contentScale = ContentScale.Crop
                )
            }

            OutlinedText(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Paddings.xsmall),
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                outlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                outlineDrawStyle = Stroke(
                    width = 12f
                )
            )
        }
    }
}

@Composable
fun FillSketchSettingButton(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    contentDescription: String? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    text: String? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .border(
                4.dp,
                MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(20.dp)
            ),
        color = color,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(Paddings.large)
                .fillMaxSize()
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            painter?.let {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Fit
                )
            }

            text?.let {
                OutlinedText(
                    modifier = Modifier
                        .padding(start = Paddings.large),
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    outlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    outlineDrawStyle = Stroke(
                        width = 12f
                    )
                )
            }
        }
    }
}

@Composable
@Preview
private fun FillSketchMainButton1Preview() {
    FillSketchTheme {
        FillSketchMainButton(
            modifier = Modifier.size(180.dp),
            text = "MyWorks",
            painter = painterResource(id = AssetR.drawable.img_myworks),
            onClick = {}
        )
    }
}

@Composable
@Preview
private fun FillSketchMainButtonWithBadgePreview() {
    FillSketchTheme {
        FillSketchMainButton(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = AssetR.drawable.img_myworks),
            badge = painterResource(id = R.drawable.ic_playstore),
            onClick = {}
        )
    }
}

@Composable
@Preview
private fun FillSketchMainButton2Preview() {
    FillSketchTheme {
        FillSketchMainButton(
            modifier = Modifier.size(180.dp),
            text = "Sketch",
            painter = painterResource(id = AssetR.drawable.img_sketch),
            onClick = {}
        )
    }
}

@Composable
@Preview
private fun FillSketchSettingButtonPreview() {
    FillSketchTheme {
        FillSketchSettingButton(
            modifier = Modifier.size(60.dp),
            painter = painterResource(id = R.drawable.ic_setting),
            onClick = {}
        )
    }
}

@Composable
@Preview
private fun FillSketchSettingButton1Preview() {
    FillSketchTheme {
        FillSketchSettingButton(
            modifier = Modifier
                .height(60.dp)
                .width(200.dp),
            painter = painterResource(id = R.drawable.ic_setting),
            onClick = {}
        )
    }
}

@Composable
@Preview
private fun FillSketchSettingButton2Preview() {
    FillSketchTheme {
        FillSketchSettingButton(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.ic_ads),
            text = "Unlock by watching an ADs",
            onClick = {}
        )
    }
}
