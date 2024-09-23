package com.dev.philo.fillsketch.feature.drawing.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme

@Composable
fun DrawingScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawingResult: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Drawing 화면"
            )

            Button(
                onClick = { onBackClick() }
            ) {
                Text(text = "Back")
            }

            Button(
                onClick = { navigateToDrawingResult() }
            ) {
                Text(text = "저장")
            }
        }
    }
}

@Preview
@Composable
fun DrawingScreenPreview() {
    FillSketchTheme {
        DrawingScreen(
            paddingValues = PaddingValues(),
            onShowErrorSnackBar = {},
            onBackClick = {},
            navigateToDrawingResult = {}
        )
    }
}