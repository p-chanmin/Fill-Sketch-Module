package com.dev.philo.fillsketch.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter.State.Empty.painter
import com.dev.philo.fillsketch.core.designsystem.R
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR
import com.dev.philo.fillsketch.asset.R as AssetR
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings

@Composable
fun FillSketchCard(
    modifier: Modifier = Modifier,
    painter: Painter,
    painterDescription: String? = null,
    isLock: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .border(5.dp, MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(8.dp))
            .padding(5.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 2.dp,
    ) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Image(
                modifier = Modifier.background(
                    if (isLock) {
                        MaterialTheme.colorScheme.scrim
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                ),
                painter = painter,
                contentDescription = painterDescription,
                contentScale = ContentScale.Fit
            )

            if (isLock) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopEnd)
                        .rotate(10f)
                        .padding(top = Paddings.large, start = Paddings.large),
                    painter = painterResource(id =  DesignSystemR.drawable.ic_lock),
                    contentDescription = "locked sketch",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
@Preview
private fun FillSketchCardPreview() {
    FillSketchTheme {
        FillSketchCard(
            modifier = Modifier,
            painter = painterResource(id =  AssetR.drawable.sketch_06_outline),
            isLock = true,
            onClick = {}
        )
    }
}

