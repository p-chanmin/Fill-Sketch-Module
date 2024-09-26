package com.dev.philo.fillsketch.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.R
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchCard
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.Sketch
import com.dev.philo.fillsketch.feature.home.component.MyWorkImage
import com.dev.philo.fillsketch.feature.home.model.MyWork
import com.dev.philo.fillsketch.feature.home.model.SketchListUiEvent
import com.dev.philo.fillsketch.feature.home.model.SketchListUiState
import com.dev.philo.fillsketch.feature.home.viewmodel.SketchListViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun SketchListScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: (Int, Int) -> Unit,
    sketchListViewModel: SketchListViewModel = hiltViewModel()
) {

    val sketchListUiState by sketchListViewModel.sketchListUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        sketchListViewModel.uiEventFlow.collectLatest { event ->
            when (event) {
                is SketchListUiEvent.NavigateToDrawing -> {
                    navigateToDrawing(event.sketchType, event.drawingResultId)
                }
            }
        }
    }

    SketchListContent(
        onShowErrorSnackBar = onShowErrorSnackBar,
        onBackClick = onBackClick,
        navigateToDrawing = navigateToDrawing,
        sketchListUiState = sketchListUiState,
        selectSketch = sketchListViewModel::selectSketch,
        unlockSketch = sketchListViewModel::unlockSketch,
        addMyWork = sketchListViewModel::addMyWork,
        deleteMyWork = sketchListViewModel::deleteMyWork
    )

}

@Composable
fun SketchListContent(
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: (Int, Int) -> Unit,
    sketchListUiState: SketchListUiState = SketchListUiState(),
    selectSketch: (Int) -> Unit,
    unlockSketch: (Int) -> Unit,
    addMyWork: (Int) -> Unit,
    deleteMyWork: (Int, Int) -> Unit,
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
                textModifier = Modifier,
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
                    .fillMaxSize()
                    .padding(Paddings.large),
                state = lazyGridState,
                columns = GridCells.Adaptive(minSize = 140.dp),
                verticalArrangement = Arrangement.spacedBy(Paddings.medium),
                horizontalArrangement = Arrangement.spacedBy(Paddings.medium),
            ) {
                items(sketchListUiState.sketchList, key = { it.sketchType }) {
                    FillSketchCard(
                        painter = painterResource(id = SketchResource.sketchOutlineResourceIds[it.sketchType]),
                        isLock = it.isLocked
                    ) {
                        selectSketch(it.sketchType)
                    }


                }
            }
        }

        if (sketchListUiState.dialogMyWorksVisible && sketchListUiState.selectedSketchId != null) {
            FillSketchDialog(
                titleText = "Select Work !",
                onDismissRequest = { selectSketch(sketchListUiState.selectedSketchId) }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        FillSketchSettingButton(
                            modifier = Modifier
                                .padding(top = Paddings.large)
                                .height(60.dp)
                                .fillMaxWidth(),
                            painter = painterResource(id = R.drawable.ic_plus),
                            color = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                addMyWork(sketchListUiState.selectedSketchId)
                            }
                        )
                    }

                    items(sketchListUiState.myWorks, key = { it.id }) {

                        var deleteDialog by remember { mutableStateOf(false) }

                        MyWorkImage(
                            sketchType = it.sketchType,
                            paths = it.paths.toPersistentList(),
                            onClick = {
                                selectSketch(sketchListUiState.selectedSketchId)
                                navigateToDrawing(it.sketchType, it.id)
                            },
                            onDeleteClick = { deleteDialog = true }
                        )

                        if (deleteDialog) {
                            FillSketchDialog(
                                onDismissRequest = { deleteDialog = false }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    OutlinedText(
                                        modifier = Modifier.padding(horizontal = Paddings.medium),
                                        textModifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = Paddings.xextra),
                                        text = "Once deleted, it can't be undone.\nIs it okay to delete?",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.tertiary,
                                            lineHeight = 20.sp
                                        ),
                                        outlineColor = MaterialTheme.colorScheme.onTertiary,
                                        outlineDrawStyle = Stroke(
                                            width = 10f,
                                        ),
                                        textAlign = TextAlign.Center
                                    )

                                    FillSketchSettingButton(
                                        modifier = Modifier
                                            .padding(top = Paddings.xextra)
                                            .height(60.dp)
                                            .width(200.dp),
                                        painter = painterResource(id = DesignSystemR.drawable.ic_trash),
                                        text = "delete",
                                        onClick = {
                                            deleteMyWork(it.sketchType, it.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (sketchListUiState.dialogUnlockVisible && sketchListUiState.selectedSketchId != null) {
            FillSketchDialog(
                onDismissRequest = { selectSketch(sketchListUiState.selectedSketchId) }
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    FillSketchCard(
                        modifier = Modifier.width(200.dp),
                        painter = painterResource(id = SketchResource.sketchOutlineResourceIds[sketchListUiState.selectedSketchId]),
                        onClick = {}
                    )
                    FillSketchSettingButton(
                        modifier = Modifier
                            .padding(top = Paddings.large)
                            .height(60.dp)
                            .fillMaxWidth(),
                        painter = painterResource(id = R.drawable.ic_ads),
                        text = "Watch Ad",
                        onClick = {
                            // 광고 보기
                            unlockSketch(sketchListUiState.selectedSketchId)
                            selectSketch(sketchListUiState.selectedSketchId)
                        }
                    )
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
            navigateToDrawing = { _, _ -> },
            sketchListUiState = SketchListUiState(
                sketchList = persistentListOf(
                    Sketch(sketchType = 0, isLocked = false),
                    Sketch(sketchType = 1),
                    Sketch(sketchType = 2),
                    Sketch(sketchType = 3, isLocked = false),
                    Sketch(sketchType = 4, isLocked = false),
                    Sketch(sketchType = 5),
                    Sketch(sketchType = 6),
                    Sketch(sketchType = 7, isLocked = false),
                    Sketch(sketchType = 8, isLocked = false),
                    Sketch(sketchType = 9),
                ),
                selectedSketchId = 0,
                myWorks = persistentListOf(
                    MyWork(0, 0),
                    MyWork(1, 0),
                    MyWork(2, 0),
                ),
                dialogUnlockVisible = true,
                dialogMyWorksVisible = false
            ),
            selectSketch = {},
            unlockSketch = {},
            addMyWork = {},
            deleteMyWork = { _, _ -> }
        )
    }
}