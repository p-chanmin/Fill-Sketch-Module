package com.dev.philo.fillsketch.feature.home.screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.home.component.MyWorkImage
import com.dev.philo.fillsketch.feature.home.model.MyWork
import com.dev.philo.fillsketch.feature.home.model.MyWorksUiState
import com.dev.philo.fillsketch.feature.home.viewmodel.MyWorksViewModel
import kotlinx.collections.immutable.persistentListOf
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun MyWorksScreen(
    paddingValues: PaddingValues,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawingResult: (Int, Int) -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
    myWorksViewModel: MyWorksViewModel = hiltViewModel()
) {
    val myWorksUiState by myWorksViewModel.myWorksUiState.collectAsStateWithLifecycle()

    MyWorksContent(
        myWorksUiState = myWorksUiState,
        onBackClick = onBackClick,
        navigateToDrawingResult = navigateToDrawingResult,
        deleteMyWork = myWorksViewModel::deleteMyWork,
        playSoundEffect = playSoundEffect,
    )
}

@Composable
fun MyWorksContent(
    myWorksUiState: MyWorksUiState,
    onBackClick: () -> Unit,
    navigateToDrawingResult: (Int, Int) -> Unit,
    deleteMyWork: (Int) -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
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
                modifier = Modifier.size(60.dp),
                playSoundEffect = playSoundEffect,
                painter = painterResource(id = DesignSystemR.drawable.ic_left),
                onClick = { onBackClick() }
            )

            OutlinedText(
                textModifier = Modifier,
                text = "My Works",
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
                items(myWorksUiState.myWorks, key = { it.id }) {

                    var deleteDialog by remember { mutableStateOf(false) }

                    MyWorkImage(
                        sketchType = it.sketchType,
                        latestBitmap = it.latestBitmap,
                        playSoundEffect = playSoundEffect,
                        onClick = { navigateToDrawingResult(it.sketchType, it.id) },
                        onDeleteClick = { deleteDialog = true },
                    )

                    if (deleteDialog) {
                        FillSketchDialog(
                            onDismissRequest = { deleteDialog = false },
                            playSoundEffect = playSoundEffect,
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
                                    playSoundEffect = playSoundEffect,
                                    painter = painterResource(id = DesignSystemR.drawable.ic_trash),
                                    text = "delete",
                                    onClick = {
                                        deleteMyWork(it.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(device = "id:pixel_4")
@Preview(device = "id:pixel_c")
@Composable
fun MyWorksContentPreview() {
    FillSketchTheme {
        MyWorksContent(
            myWorksUiState = MyWorksUiState(
                myWorks = persistentListOf(
                    MyWork(
                        id = 0,
                        sketchType = 0,
                        latestBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                            .asAndroidBitmap()
                    ),
                    MyWork(
                        id = 1,
                        sketchType = 1,
                        latestBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[1])
                            .asAndroidBitmap()
                    )
                )
            ),
            onBackClick = {},
            navigateToDrawingResult = { _, _ -> },
            deleteMyWork = {}
        )
    }
}