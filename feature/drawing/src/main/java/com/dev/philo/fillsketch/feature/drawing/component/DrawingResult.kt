package com.dev.philo.fillsketch.feature.drawing.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme

@Composable
fun DrawingResultImage(
    modifier: Modifier = Modifier,
    resultBitmap: Bitmap,
) {
    Surface(
        modifier = modifier
            .border(
                5.dp,
                MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(5.dp),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 2.dp,
    ) {
        Box {
            Image(
                bitmap = resultBitmap.asImageBitmap(),
                contentDescription = null
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun DrawingResultImagePreview() {
    FillSketchTheme {
        DrawingResultImage(
            resultBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                .asAndroidBitmap()
        )
    }
}