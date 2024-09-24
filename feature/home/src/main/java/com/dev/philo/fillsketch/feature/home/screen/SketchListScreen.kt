package com.dev.philo.fillsketch.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchCard
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.feature.home.R
import com.dev.philo.fillsketch.feature.home.model.SketchListUiState
import com.dev.philo.fillsketch.feature.home.viewmodel.SketchListViewModel
import kotlinx.collections.immutable.toPersistentList
import com.dev.philo.fillsketch.asset.R as AssetR
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun SketchListScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: () -> Unit,
    sketchListViewModel: SketchListViewModel = hiltViewModel()
) {

    val sketchListUiState by sketchListViewModel.sketchListUiState.collectAsStateWithLifecycle()

    SketchListContent(
        onShowErrorSnackBar = onShowErrorSnackBar,
        onBackClick = onBackClick,
        navigateToDrawing = navigateToDrawing,
        sketchListUiState = sketchListUiState
    )

}

@Composable
fun SketchListContent(
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: () -> Unit,
    sketchListUiState: SketchListUiState = SketchListUiState()
) {

    val lazyGridState = rememberLazyGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Paddings.xlarge, start = Paddings.large, end = Paddings.large),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FillSketchSettingButton(
                modifier = Modifier
                    .size(60.dp),
                painter = painterResource(id = DesignSystemR.drawable.ic_left),
                onClick = { onBackClick() }
            )

            OutlinedText(
                modifier = Modifier,
                text = "Select Sketch !",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 35.sp,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                outlineColor = MaterialTheme.colorScheme.onTertiary,
                outlineDrawStyle = Stroke(
                    width = 15f
                )
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(Paddings.extra),
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 4.dp,
            color = MaterialTheme.colorScheme.secondary
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize().padding(Paddings.large),
                state = lazyGridState,
                columns = GridCells.Adaptive(minSize = 150.dp),
                verticalArrangement = Arrangement.spacedBy(Paddings.medium),
                horizontalArrangement = Arrangement.spacedBy(Paddings.medium),
            ) {
                items(sketchListUiState.sketchList, key = { it }) {
                    FillSketchCard(
                        painter = painterResource(id = it),
                        isLock = true
                    ) {
                        
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Preview(device = "id:pixel_c")
@Composable
fun SketchListContentPreview() {
    FillSketchTheme {
        SketchListContent(
            onShowErrorSnackBar = {},
            onBackClick = {},
            navigateToDrawing = {},
            sketchListUiState = SketchListUiState(
                sketchList = SketchResource.sketchOutlineResourceIds.toPersistentList()
            )
        )
    }
}