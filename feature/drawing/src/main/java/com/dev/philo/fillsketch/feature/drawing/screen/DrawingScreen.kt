package com.dev.philo.fillsketch.feature.drawing.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.drawing.R
import com.dev.philo.fillsketch.feature.drawing.component.CheckeredBackgroundBox
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
    drawingResultId: Long,
    onShowErrorSnackBar: (message: String) -> Unit,
    onBackClick: () -> Unit,
    navigateToDrawingResult: (Int, Long) -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
    showMagicRewardAd: (() -> Unit, () -> Unit) -> Unit,
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
            recommendImageBitmap,
            outlineImageBitmap,
        )
    }

    val drawingUiState by drawingViewModel.drawingUiState.collectAsStateWithLifecycle()

    DrawingContent(
        modifier = Modifier.fillMaxSize(),
        drawingUiState = drawingUiState,
        onShowErrorSnackBar = onShowErrorSnackBar,
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
        playSoundEffect = playSoundEffect,
        showMagicRewardAd = showMagicRewardAd,
    )
}

@Composable
fun DrawingContent(
    modifier: Modifier = Modifier,
    drawingUiState: DrawingUiState,
    onShowErrorSnackBar: (message: String) -> Unit,
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
    playSoundEffect: (SoundEffect) -> Unit = {},
    showMagicRewardAd: (() -> Unit, () -> Unit) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        val context = LocalContext.current
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
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
                playSoundEffect = playSoundEffect,
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
                    playSoundEffect = playSoundEffect,
                    painter = painterResource(id = DesignSystemR.drawable.ic_undo),
                    enabled = undoPathSize != 0,
                    onClick = { undo() }
                )
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    playSoundEffect = playSoundEffect,
                    painter = painterResource(id = DesignSystemR.drawable.ic_redo),
                    enabled = redoPathSize != 0,
                    onClick = { redo() }
                )
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    playSoundEffect = playSoundEffect,
                    painter = painterResource(id = DesignSystemR.drawable.ic_trash),
                    onClick = { resetDialogVisible = true }
                )
                DrawingUiButton(
                    modifier = Modifier
                        .height(30.dp)
                        .width(60.dp),
                    playSoundEffect = playSoundEffect,
                    painter = painterResource(id = DesignSystemR.drawable.ic_complete),
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
                playSoundEffect = playSoundEffect,
                colorPickerController = colorPickerController,
                currentResultBitmap = drawingUiState.currentResultBitmap,
                initialColor = drawingUiState.strokeColor
            )
        }

        DrawingPalette(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Paddings.large + 60.dp)
                .fillMaxWidth(),
            drawingUiState = drawingUiState,
            playSoundEffect = playSoundEffect,
            colorPickerController = colorPickerController,
            openColorPickerDialog = { colorPaletteDialog = true },
            updateActionType = updateActionType,
            updateStrokeWidth = updateStrokeWidth,
            updateMagicBrushState = {
                showMagicRewardAd(
                    { updateMagicBrushState() },
                    { onShowErrorSnackBar(context.getString(R.string.feature_drawing_ad_load_fail)) }
                )
            },
            initOffset = {
                offset = Offset.Zero
                scale = 1f
            }
        )

        if (resetDialogVisible) {
            FillSketchDialog(
                onDismissRequest = { resetDialogVisible = false },
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
                        text = stringResource(R.string.feature_drawing_reset_description),
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
                        text = stringResource(R.string.feature_drawing_reset),
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
    insertNewPath: (Offset) -> Unit,
    updateLatestPath: (Offset) -> Unit,
    drawOnNewMask: () -> Unit,
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
    ) {
        CheckeredBackgroundBox(tileSize = 70.dp)
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
                bitmap = drawingUiState.currentResultBitmap.asImageBitmap(),
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
                currentResultBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                    .asAndroidBitmap(),
                width = 1024,
                height = 1536
            ),
            onShowErrorSnackBar = {},
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
            showMagicRewardAd = { _, _ -> }
        )
    }
}