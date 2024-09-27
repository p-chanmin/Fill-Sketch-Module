package com.dev.philo.fillsketch.feature.drawing.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.utils.createPath
import com.dev.philo.fillsketch.core.designsystem.utils.getEmptyBitmapBySize
import com.dev.philo.fillsketch.core.model.ActionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun DrawingResultImage(
    modifier: Modifier = Modifier,
    sketchType: Int,
    latestBitmap: Bitmap,
) {

    val recommendImageBitmap =
        ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[sketchType])

    val outlineImageBitmap =
        ImageBitmap.imageResource(id = SketchResource.sketchOutlineResourceIds[sketchType])

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
                bitmap = recommendImageBitmap,
                contentDescription = null
            )

            Image(
                bitmap = latestBitmap.asImageBitmap(),
                contentDescription = null
            )

            Image(
                bitmap = outlineImageBitmap,
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
            sketchType = 0,
            latestBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0]).asAndroidBitmap()
        )
    }
}