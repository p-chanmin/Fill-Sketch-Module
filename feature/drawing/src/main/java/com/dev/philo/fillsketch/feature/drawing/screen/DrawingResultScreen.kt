package com.dev.philo.fillsketch.feature.drawing.screen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.drawing.R
import com.dev.philo.fillsketch.feature.drawing.component.DrawingResultImage
import com.dev.philo.fillsketch.feature.drawing.model.DrawingResultUiState
import com.dev.philo.fillsketch.feature.drawing.viewmodel.DrawingResultViewModel
import kotlinx.coroutines.launch
import com.dev.philo.fillsketch.asset.R as AssetR
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun DrawingResultScreen(
    paddingValues: PaddingValues,
    sketchType: Int,
    drawingResultId: Long,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawing: (Int, Long) -> Unit,
    navigateToMyWorks: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
    showInterstitialRewardAd: () -> Unit,
    drawingResultViewModel: DrawingResultViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val sketchDpi =
        ImageBitmap.imageResource(SketchResource.sketchRecommendResourceIds[sketchType])
            .asAndroidBitmap().density

    LaunchedEffect(Unit) {
        drawingResultViewModel.fetchDrawingUiState(
            sketchType,
            drawingResultId,
            sketchDpi
        )
    }

    val drawingResultUiState by drawingResultViewModel.drawingResultUiState.collectAsStateWithLifecycle()

    DrawingResultContent(
        drawingResultUiState = drawingResultUiState,
        navigateToDrawing = { navigateToDrawing(sketchType, drawingResultId) },
        dismissSaveCompleteDialog = { drawingResultViewModel.updateSaveCompleteDialogVisible(false) },
        navigateToMyWorks = navigateToMyWorks,
        onBackClick = onBackClick,
        playSoundEffect = playSoundEffect,
        saveDrawingResult = {
            coroutineScope.launch {
                val resultBitmap = drawingResultUiState.resultBitmap

                val timeStamp = System.currentTimeMillis()
                val imageFileName =
                    "${context.getString(AssetR.string.asset_title)}_$timeStamp.jpg"

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "Pictures/${context.getString(AssetR.string.asset_title)}"
                    )
                }

                val uri: Uri? =
                    context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                uri?.let {
                    context.contentResolver.openOutputStream(it).use { outputStream ->
                        outputStream?.let {
                            resultBitmap.compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                it
                            )
                        }
                    }
                }
                showInterstitialRewardAd()
                drawingResultViewModel.updateSaveCompleteDialogVisible(true)
            }
        }
    )
}

@Composable
fun DrawingResultContent(
    drawingResultUiState: DrawingResultUiState,
    navigateToDrawing: () -> Unit,
    dismissSaveCompleteDialog: () -> Unit,
    onBackClick: () -> Unit,
    saveDrawingResult: () -> Unit,
    navigateToMyWorks: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = !drawingResultUiState.isLoading,
            enter = fadeIn(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(Paddings.xlarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedText(
                    textModifier = Modifier,
                    text = stringResource(R.string.feature_drawing_download_your_drawing),
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                    ),
                    outlineColor = MaterialTheme.colorScheme.onTertiary,
                    outlineDrawStyle = Stroke(
                        width = 15f
                    ),
                    textAlign = TextAlign.Center
                )

                DrawingResultImage(
                    modifier = Modifier
                        .width(400.dp)
                        .padding(Paddings.xextra),
                    resultBitmap = drawingResultUiState.resultBitmap
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    FillSketchSettingButton(
                        modifier = Modifier
                            .height(60.dp)
                            .width(60.dp),
                        playSoundEffect = playSoundEffect,
                        painter = painterResource(id = DesignSystemR.drawable.ic_edit),
                        onClick = { navigateToDrawing() }
                    )
                    FillSketchSettingButton(
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .height(60.dp)
                            .width(200.dp),
                        playSoundEffect = playSoundEffect,
                        painter = painterResource(id = DesignSystemR.drawable.ic_download),
                        onClick = { saveDrawingResult() }
                    )
                }
            }
        }

        FillSketchSettingButton(
            modifier = Modifier
                .padding(top = Paddings.xlarge, start = Paddings.large)
                .size(60.dp),
            playSoundEffect = playSoundEffect,
            painter = painterResource(id = DesignSystemR.drawable.ic_left),
            onClick = { onBackClick() }
        )
    }

    if (drawingResultUiState.saveCompleteDialogVisible) {
        FillSketchDialog(
            onDismissRequest = { dismissSaveCompleteDialog() },
            playSoundEffect = playSoundEffect,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedText(
                    textModifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Paddings.xextra),
                    text = stringResource(R.string.feature_drawing_download_complete),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                    ),
                    outlineColor = MaterialTheme.colorScheme.onTertiary,
                    outlineDrawStyle = Stroke(
                        width = 15f,
                    ),
                    textAlign = TextAlign.Center
                )

                FillSketchSettingButton(
                    modifier = Modifier
                        .padding(top = Paddings.xextra)
                        .height(60.dp)
                        .fillMaxWidth(),
                    playSoundEffect = playSoundEffect,
                    text = stringResource(R.string.feature_drawing_move_to_myworks),
                    onClick = {
                        navigateToMyWorks()
                    }
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(device = "id:pixel_4")
@Preview(device = "id:pixel_c")
@Composable
fun DrawingResultContentPreview() {
    FillSketchTheme {
        DrawingResultContent(
            drawingResultUiState = DrawingResultUiState(
                isLoading = false,
                saveCompleteDialogVisible = false,
                resultBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                    .asAndroidBitmap()
            ),
            navigateToDrawing = {},
            dismissSaveCompleteDialog = {},
            onBackClick = {},
            saveDrawingResult = {},
            navigateToMyWorks = {},
        )
    }
}