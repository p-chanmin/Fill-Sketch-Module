package com.dev.philo.fillsketch.feature.home.screen

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
fun SketchListScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "스케치 목록 화면"
            )

            Button(
                onClick = { onBackClick() }
            ) {
                Text(text = "Back")
            }

            Button(
                onClick = navigateToDrawing
            ) {
                Text(text = "스케치 선택")
            }
        }
    }

}

@Preview
@Composable
fun SketchListScreenPreview() {
    FillSketchTheme {
        SketchListScreen(
            paddingValues = PaddingValues(),
            onShowErrorSnackBar = {},
            onBackClick = {},
            navigateToDrawing = {}
        )
    }
}