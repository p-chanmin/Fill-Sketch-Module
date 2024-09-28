package com.dev.philo.fillsketch.feature.home.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.R
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.SoundEffect

@Composable
fun MyWorkImage(
    modifier: Modifier = Modifier,
    sketchType: Int,
    latestBitmap: Bitmap,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
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
            .padding(5.dp)
            .clickable {
                playSoundEffect(SoundEffect.BUTTON_CLICK)
                onClick()
            },
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

            FillSketchSettingButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = Paddings.medium, end = Paddings.medium)
                    .size(40.dp),
                painter = painterResource(id = R.drawable.ic_trash),
                color = MaterialTheme.colorScheme.primary,
                onClick = { onDeleteClick() },
                playSoundEffect = playSoundEffect,
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun MyWorkImagePreview() {
    FillSketchTheme {
        MyWorkImage(
            sketchType = 0,
            latestBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                .asAndroidBitmap(),
            onClick = {},
            onDeleteClick = {}
        )
    }
}