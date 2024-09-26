package com.dev.philo.fillsketch.feature.drawing.screen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchSettingButton
import com.dev.philo.fillsketch.core.designsystem.component.OutlinedText
import com.dev.philo.fillsketch.core.designsystem.model.PathWrapper
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.R
import com.dev.philo.fillsketch.feature.drawing.component.DrawingResultImage
import com.dev.philo.fillsketch.feature.drawing.model.DrawingResultUiState
import com.dev.philo.fillsketch.feature.drawing.viewmodel.DrawingResultViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun DrawingResultScreen(
    paddingValues: PaddingValues,
    sketchType: Int,
    drawingResultId: Int,
    onShowErrorSnackBar: (message: String) -> Unit,
    navigateToDrawing: (Int, Int) -> Unit,
    navigateToMyWorks: () -> Unit,
    drawingResultViewModel: DrawingResultViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val recommendImageBitmap =
        ImageBitmap.imageResource(SketchResource.sketchRecommendResourceIds[sketchType])
    val outlineImageBitmap =
        ImageBitmap.imageResource(SketchResource.sketchOutlineResourceIds[sketchType])

    LaunchedEffect(Unit) {
        drawingResultViewModel.fetchDrawingUiState(drawingResultId)
    }

    val drawingResultUiState by drawingResultViewModel.drawingResultUiState.collectAsStateWithLifecycle()

    DrawingResultContent(
        sketchType = sketchType,
        drawingResultUiState = drawingResultUiState,
        navigateToDrawing = { navigateToDrawing(sketchType, drawingResultId) },
        dismissSaveCompleteDialog = { drawingResultViewModel.updateSaveCompleteDialogVisible(false) },
        navigateToMyWorks = navigateToMyWorks,
        saveDrawingResult = {
            coroutineScope.launch {
                val resultBitmap =
                    drawingResultViewModel.getResultBitmap(recommendImageBitmap, outlineImageBitmap)

                val timeStamp = System.currentTimeMillis()
                val imageFileName =
                    "${context.getString(R.string.feature_drawing_save_title)}_$timeStamp.jpg"

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        "Pictures/${context.getString(com.dev.philo.fillsketch.feature.drawing.R.string.feature_drawing_save_title)}"
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
                                Bitmap.CompressFormat.JPEG,
                                100,
                                it
                            )
                        }
                    }
                }

                drawingResultViewModel.updateSaveCompleteDialogVisible(true)
            }
        }
    )
}

@Composable
fun DrawingResultContent(
    sketchType: Int,
    drawingResultUiState: DrawingResultUiState,
    navigateToDrawing: () -> Unit,
    dismissSaveCompleteDialog: () -> Unit,
    saveDrawingResult: () -> Unit,
    navigateToMyWorks: () -> Unit,
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
            text = "Download Your Drawing !",
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
            sketchType = sketchType,
            paths = drawingResultUiState.paths
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
                painter = painterResource(id = DesignSystemR.drawable.ic_edit),
                onClick = { navigateToDrawing() }
            )
            FillSketchSettingButton(
                modifier = Modifier
                    .padding(start = 32.dp)
                    .height(60.dp)
                    .width(200.dp),
                painter = painterResource(id = DesignSystemR.drawable.ic_download),
                onClick = { saveDrawingResult() }
            )
        }
    }
    if (drawingResultUiState.saveCompleteDialogVisible) {
        FillSketchDialog(
            onDismissRequest = { dismissSaveCompleteDialog() }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedText(
                    textModifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Paddings.xextra),
                    text = "Download Complete !",
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
                    text = "Move To MyWorks",
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
            sketchType = 0,
            drawingResultUiState = DrawingResultUiState(
                saveCompleteDialogVisible = true,
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
                        alpha = 1f
                    )
                )
            ),
            navigateToDrawing = {},
            dismissSaveCompleteDialog = {},
            saveDrawingResult = {},
            navigateToMyWorks = {}
        )
    }
}