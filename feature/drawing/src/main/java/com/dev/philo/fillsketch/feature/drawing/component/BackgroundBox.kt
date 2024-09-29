package com.dev.philo.fillsketch.feature.drawing.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme

@Composable
fun CheckeredBackgroundBox(tileSize: Dp) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val color1 = MaterialTheme.colorScheme.background
        val color2 = MaterialTheme.colorScheme.primary
        Canvas(modifier = Modifier.fillMaxSize()) {
            val numRows = (size.height / tileSize.toPx()).toInt()
            val numCols = (size.width / tileSize.toPx()).toInt()
            for (row in 0 until numRows + 1) {
                for (col in 0 until numCols + 1) {
                    val color = if ((row + col) % 2 == 0) {
                        color1
                    } else {
                        color2
                    }
                    drawRect(
                        color = color,
                        topLeft = Offset(x = col * tileSize.toPx(), y = row * tileSize.toPx()),
                        size = Size(tileSize.toPx(), tileSize.toPx())
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun CheckeredBackgroundBoxPreview() {
    FillSketchTheme {
        CheckeredBackgroundBox(70.dp)
    }
}