package com.dev.philo.fillsketch.feature.drawing.screen

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
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
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.component.ColorPickerDialog
import com.dev.philo.fillsketch.feature.drawing.component.DrawingPalette
import com.dev.philo.fillsketch.feature.drawing.component.DrawingUiButton
import com.dev.philo.fillsketch.feature.drawing.model.DrawingUiState
import com.dev.philo.fillsketch.feature.drawing.viewmodel.DrawingViewModel
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.flow.collectLatest
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

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
            sketchType,
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
        updateColor = drawingViewModel::updateColor,
        updateStrokeWidth = drawingViewModel::updateStrokeWidth,
        updateActionType = drawingViewModel::updateActionType,
        updateMagicBrushState = drawingViewModel::updateMagicBrushState,
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
    updateColor: (Color) -> Unit,
    updateStrokeWidth: (Float) -> Unit,
    updateActionType: (ActionType) -> Unit,
    updateMagicBrushState: () -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawingResult: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        var paletteVisible by remember { mutableStateOf(true) }
        var resetDialogVisible by remember { mutableStateOf(false) }

        val minScale = 0.6f
        val maxScale = 8f

        DrawingCanvas(
            scale = scale,
            offset = offset,
            onDrag = { pan, zoom ->
                scale = (scale * zoom).coerceIn(minScale, maxScale)
                offset = Offset((offset.x + pan.x * scale), (offset.y + pan.y * scale))
            },
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
                .padding(top = Paddings.extra)
                .padding(horizontal = Paddings.large),
        ) {
            DrawingUiButton(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = DesignSystemR.drawable.ic_left),
                onClick = { onBackClick() }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    painter = painterResource(id = DesignSystemR.drawable.ic_undo),
                    enabled = undoPathSize != 0,
                    onClick = { undo() }
                )
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    painter = painterResource(id = DesignSystemR.drawable.ic_redo),
                    enabled = redoPathSize != 0,
                    onClick = { redo() }
                )
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    painter = painterResource(id = DesignSystemR.drawable.ic_trash),
                    enabled = undoPathSize != 0,
                    onClick = { resetDialogVisible = true }
                )
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    painter = painterResource(id = DesignSystemR.drawable.ic_complete),
                    enabled = undoPathSize != 0,
                    onClick = { navigateToDrawingResult() }
                )
            }
        }

        val colorPickerController = rememberColorPickerController()
        var colorPaletteDialog by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            colorPickerController.selectByColor(drawingUiState.strokeColor, false)
            colorPickerController.getColorFlow().collectLatest { colorEnvelope ->
                updateColor(colorEnvelope.color)
                updateActionType(ActionType.BRUSH)
            }
        }

        if (colorPaletteDialog) {
            ColorPickerDialog(
                onDismiss = { colorPaletteDialog = false },
                colorPickerController = colorPickerController,
                sketchType = drawingUiState.sketchType,
                currentMaskBitmap = currentMaskBitmap,
                initialColor = drawingUiState.strokeColor
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Paddings.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = Paddings.large)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalArrangement = Arrangement.spacedBy(Paddings.large)
                ) {
                    DrawingUiButton(
                        modifier = Modifier.size(30.dp),
                        painter = if (paletteVisible) {
                            painterResource(id = DesignSystemR.drawable.ic_hide_down)
                        } else {
                            painterResource(id = DesignSystemR.drawable.ic_hide_up)
                        },
                        onClick = {
                            paletteVisible = !paletteVisible
                        }
                    )
                    DrawingUiButton(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = DesignSystemR.drawable.ic_screen),
                        onClick = {
                            offset = Offset.Zero
                            scale = 1f
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = paletteVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                DrawingPalette(
                    drawingUiState = drawingUiState,
                    colorPickerController = colorPickerController,
                    openColorPickerDialog = { colorPaletteDialog = true },
                    updateActionType = updateActionType,
                    updateStrokeWidth = updateStrokeWidth,
                    updateMagicBrushState = updateMagicBrushState,
                )
            }
        }

        if (resetDialogVisible) {
            FillSketchDialog(
                onDismissRequest = { resetDialogVisible = false }
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
                        text = "Once you reset, you cannot revert.\nDo you want to reset?",
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
                        text = "reset",
                        onClick = {
                            reset()
                            resetDialogVisible = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DrawingCanvas(
    scale: Float,
    offset: Offset,
    onDrag: (Offset, Float) -> Unit,
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
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawingUiState.actionType) {
                if (drawingUiState.actionType == ActionType.MOVE) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        onDrag(pan, zoom)
                    }
                }
            }
            .background(MaterialTheme.colorScheme.scrim)
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
            updateColor = { },
            updateStrokeWidth = { },
            updateActionType = { },
            updateMagicBrushState = { },
            onBackClick = { },
            navigateToDrawingResult = { },
        )
    }
}