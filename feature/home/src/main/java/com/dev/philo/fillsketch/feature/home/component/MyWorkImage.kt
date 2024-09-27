package com.dev.philo.fillsketch.feature.home.component

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.designsystem.utils.createPath
import com.dev.philo.fillsketch.core.designsystem.utils.getEmptyBitmapBySize
import com.dev.philo.fillsketch.core.model.ActionType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MyWorkImage(
    modifier: Modifier = Modifier,
    sketchType: Int,
    paths: ImmutableList<PathWrapper>,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    val recommendImageBitmap =
        ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[sketchType])

    val outlineImageBitmap =
        ImageBitmap.imageResource(id = SketchResource.sketchOutlineResourceIds[sketchType])

    val maskBitmap = getEmptyBitmapBySize(
        outlineImageBitmap.width,
        outlineImageBitmap.height,
        outlineImageBitmap.asAndroidBitmap().density,
        whiteBackground = true
    )

    val canvas = Canvas(maskBitmap)

    paths.forEach { path ->
        val paint = Paint().apply {

            if (path.actionType == ActionType.MAGIC_BRUSH) {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            }

            color = if (path.actionType == ActionType.ERASER) {
                android.graphics.Color.WHITE
            } else {
                android.graphics.Color.argb(
                    (path.strokeColor.alpha * 255).toInt(),
                    (path.strokeColor.red * 255).toInt(),
                    (path.strokeColor.green * 255).toInt(),
                    (path.strokeColor.blue * 255).toInt()
                )
            }
            strokeWidth = path.strokeWidth
            style = Style.STROKE
            isAntiAlias = true

            strokeCap = Paint.Cap.ROUND
        }

        val pathObj = createPath(path.points.map { Offset(it.x, it.y) })

        canvas.drawPath(pathObj, paint)
    }
    Surface(
        modifier = modifier
            .border(
                5.dp,
                MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(5.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 2.dp,
    ) {
        Box {
            Image(
                bitmap = recommendImageBitmap,
                contentDescription = null
            )

            Image(
                bitmap = maskBitmap.asImageBitmap(),
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
                onClick = { onDeleteClick() }
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
            paths = persistentListOf(
                PathWrapper(
                    points = mutableStateListOf(
                        Offset(0f, 0f),
                        Offset(0f, 0f),
                        Offset(1000f, 1000f),
                    ),
                    strokeWidth = 40f,
                    strokeColor = Color.Red,
                    actionType = ActionType.BRUSH,
                )
            ),
            onClick = {},
            onDeleteClick = {}
        )
    }
}