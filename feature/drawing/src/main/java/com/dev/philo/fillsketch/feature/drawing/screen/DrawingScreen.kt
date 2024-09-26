package com.dev.philo.fillsketch.feature.drawing.screen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.model.DrawingUiState
import com.dev.philo.fillsketch.feature.drawing.viewmodel.DrawingViewModel

@Composable
fun DrawingScreen(
    paddingValues: PaddingValues,
    sketchType: Int,
    drawingResultId: Int,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawingResult: (Int, Int) -> Unit,
    drawingViewModel: DrawingViewModel = hiltViewModel()
) {

    val recommendImageBitmap =
        ImageBitmap.imageResource(SketchResource.sketchRecommendResourceIds[sketchType])
    val outlineImageBitmap =
        ImageBitmap.imageResource(SketchResource.sketchOutlineResourceIds[sketchType])

    LaunchedEffect(Unit) {
        drawingViewModel.fetchDrawingUiState(
            drawingResultId,
            recommendImageBitmap.width,
            recommendImageBitmap.height,
            recommendImageBitmap.asAndroidBitmap().density
        )
    }

    val drawingUiState by drawingViewModel.drawingUiState.collectAsStateWithLifecycle()
    val currentMaskBitmap by drawingViewModel.currentMaskBitmap
    val liveDrawingMaskBitmap by drawingViewModel.liveDrawingMaskBitmap

    DrawingContent(
        modifier = Modifier.fillMaxSize(),
        drawingUiState = drawingUiState,
        outlineImageBitmap = outlineImageBitmap,
        recommendImageBitmap = recommendImageBitmap,
        currentMaskBitmap = currentMaskBitmap,
        liveDrawingMaskBitmap = liveDrawingMaskBitmap,
        redoPathSize = drawingViewModel.redoPathSize,
        undoPathSize = drawingViewModel.undoPathSize,
        insertNewPath = drawingViewModel::insertNewPath,
        updateLatestPath = drawingViewModel::updateLatestPath,
        drawOnNewMask = drawingViewModel::drawOnNewMask,
        undo = drawingViewModel::undo,
        redo = drawingViewModel::redo,
        reset = drawingViewModel::reset,
        updateAlpha = drawingViewModel::updateAlpha,
        updateColor = drawingViewModel::updateColor,
        updateStrokeWidth = drawingViewModel::updateStrokeWidth,
        updateActionType = drawingViewModel::updateActionType,
        onBackClick = onBackClick,
        navigateToDrawingResult = { navigateToDrawingResult(sketchType, drawingResultId) },
    )
}

@Composable
fun DrawingContent(
    modifier: Modifier = Modifier,
    drawingUiState: DrawingUiState,
    outlineImageBitmap: ImageBitmap,
    recommendImageBitmap: ImageBitmap,
    currentMaskBitmap: Bitmap,
    liveDrawingMaskBitmap: Bitmap,
    redoPathSize: Int,
    undoPathSize: Int,
    insertNewPath: (Offset) -> Unit,
    updateLatestPath: (Offset) -> Unit,
    drawOnNewMask: () -> Unit,
    undo: () -> Unit,
    redo: () -> Unit,
    reset: () -> Unit,
    updateAlpha: (Float) -> Unit,
    updateColor: (Color) -> Unit,
    updateStrokeWidth: (Float) -> Unit,
    updateActionType: (ActionType) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawingResult: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        DrawingCanvas(
            drawingUiState = drawingUiState,
            outlineImageBitmap = outlineImageBitmap,
            recommendImageBitmap = recommendImageBitmap,
            currentMaskBitmap = currentMaskBitmap,
            liveDrawingMaskBitmap = liveDrawingMaskBitmap,
            insertNewPath = insertNewPath,
            updateLatestPath = updateLatestPath,
            drawOnNewMask = drawOnNewMask,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onBackClick() }
            ) {
                Text(text = "Back")
            }
            Button(
                enabled = undoPathSize != 0,
                onClick = { undo() }
            ) {
                Text(text = "UnDo")
            }
            Button(
                enabled = redoPathSize != 0,
                onClick = { redo() }
            ) {
                Text(text = "ReDo")
            }
            Button(
                enabled = undoPathSize != 0,
                onClick = { reset() }
            ) {
                Text(text = "Reset")
            }

            Button(
                enabled = undoPathSize != 0,
                onClick = {
                    navigateToDrawingResult()
                }
            ) {
                Text(text = "Save")
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier,
            ) {
                Button(
                    onClick = {
                        val colors = listOf(
                            Color.Red,
                            Color.Blue,
                            Color.Green,
                            Color.Yellow,
                            Color.Cyan,
                            Color.Magenta
                        )
                        val color = colors.random()
                        updateColor(color)
                        updateActionType(ActionType.BRUSH)
                    }
                ) {
                    Text(text = "Color Change")
                }
            }
            Row {
                Button(
                    enabled = drawingUiState.actionType != ActionType.MOVE,
                    onClick = { updateActionType(ActionType.MOVE) }
                ) {
                    Text(text = "Move")
                }
                Button(
                    enabled = drawingUiState.actionType != ActionType.BRUSH,
                    onClick = {
                        updateActionType(ActionType.BRUSH)
                    }
                ) {
                    Text(text = "Brush")
                }
                Button(
                    enabled = drawingUiState.actionType != ActionType.ERASER,
                    onClick = {
                        updateActionType(ActionType.ERASER)
                    }
                ) {
                    Text(text = "ERASER")
                }
                Button(
                    enabled = drawingUiState.actionType != ActionType.MAGIC_BRUSH,
                    onClick = {
                        updateActionType(ActionType.MAGIC_BRUSH)
                    }
                ) {
                    Text(text = "Magic")
                }
            }
        }
    }
}

@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawingUiState: DrawingUiState,
    outlineImageBitmap: ImageBitmap,
    recommendImageBitmap: ImageBitmap,
    currentMaskBitmap: Bitmap,
    liveDrawingMaskBitmap: Bitmap,
    insertNewPath: (Offset) -> Unit,
    updateLatestPath: (Offset) -> Unit,
    drawOnNewMask: () -> Unit
) {

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    val minScale = 0.6f
    val maxScale = 8f

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawingUiState.actionType) {
                if (drawingUiState.actionType == ActionType.MOVE) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(minScale, maxScale)
                        offset = Offset((offset.x + pan.x * scale), (offset.y + pan.y * scale))
                    }
                }
            }
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .onGloballyPositioned { coordinates ->
                    canvasSize = coordinates.size
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {

            Image(
                bitmap = recommendImageBitmap,
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .pointerInput(drawingUiState.actionType) {
                        when (drawingUiState.actionType) {
                            ActionType.BRUSH, ActionType.ERASER, ActionType.MAGIC_BRUSH -> {
                                awaitEachGesture {
                                    val start = awaitFirstDown()
                                    val startOffset = Offset(
                                        drawingUiState.width * start.position.x / canvasSize.width,
                                        drawingUiState.height * start.position.y / canvasSize.height
                                    )
                                    insertNewPath(startOffset)
                                    updateLatestPath(startOffset)

                                    drag(start.id) { change ->
                                        val calculatedOffset = Offset(
                                            drawingUiState.width * change.position.x / canvasSize.width,
                                            drawingUiState.height * change.position.y / canvasSize.height
                                        )
                                        change.consume()
                                        updateLatestPath(calculatedOffset)
                                    }
                                    drawOnNewMask()
                                }
                            }

                            else -> {}
                        }
                    },
                bitmap = currentMaskBitmap.asImageBitmap(),
                contentDescription = null
            )

            Image(
                bitmap = liveDrawingMaskBitmap.asImageBitmap(),
                contentDescription = null
            )

            Image(
                bitmap = outlineImageBitmap,
                contentDescription = null
            )
        }

    }
}

@Preview(device = "id:pixel_4")
@Preview(device = "id:pixel_c")
@Composable
fun DrawingContentPreview() {
    FillSketchTheme {
        DrawingContent(
            drawingUiState = DrawingUiState(
                width = 1024,
                height = 1536
            ),
            outlineImageBitmap = ImageBitmap.imageResource(id = SketchResource.sketchOutlineResourceIds[0]),
            recommendImageBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0]),
            currentMaskBitmap = ImageBitmap.imageResource(id = SketchResource.sketchOutlineResourceIds[0])
                .asAndroidBitmap(),
            liveDrawingMaskBitmap = ImageBitmap.imageResource(id = SketchResource.sketchOutlineResourceIds[0])
                .asAndroidBitmap(),
            redoPathSize = 10,
            undoPathSize = 10,
            insertNewPath = { },
            updateLatestPath = { },
            drawOnNewMask = { },
            undo = { },
            redo = { },
            reset = { },
            updateAlpha = { },
            updateColor = { },
            updateStrokeWidth = { },
            updateActionType = { },
            onBackClick = { },
            navigateToDrawingResult = { },
        )
    }
}