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
fun DrawingResultScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    navigateToDrawing: () -> Unit,
    navigateToMyWorks: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "그림 표시"
            )

            Button(
                onClick = { navigateToDrawing() }
            ) {
                Text(text = "편집으로")
            }

            Button(
                onClick = { navigateToMyWorks() }
            ) {
                Text(text = "저장")
            }
        }
    }
}

@Preview
@Composable
fun DrawingResultScreenPreview() {
    FillSketchTheme {
        DrawingResultScreen(
            paddingValues = PaddingValues(),
            onShowErrorSnackBar = {},
            navigateToDrawing = {},
            navigateToMyWorks = {}
        )
    }
}