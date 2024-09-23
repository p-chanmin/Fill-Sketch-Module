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
fun HomeScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    navigateToSketchList: () -> Unit,
    navigateToMyWorks: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "홈 화면",
            )

            Button(
                onClick = { navigateToSketchList() }
            ) {
                Text(text = "스케치 선택")
            }

            Button(
                onClick = { navigateToMyWorks() }
            ) {
                Text(text = "나의 작업물")
            }

            Button(
                onClick = { onShowErrorSnackBar("test message") }
            ) {
                Text(text = "test message")
            }
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    FillSketchTheme {
        HomeScreen(
            paddingValues = PaddingValues(),
            onShowErrorSnackBar = {},
            navigateToSketchList = {},
            navigateToMyWorks = {}
        )
    }
}