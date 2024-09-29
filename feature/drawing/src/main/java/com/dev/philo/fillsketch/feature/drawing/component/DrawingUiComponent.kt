package com.dev.philo.fillsketch.feature.drawing.component

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.asset.SketchResource
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.core.model.SoundEffect
import com.dev.philo.fillsketch.feature.drawing.model.DrawingUiState
import com.dev.philo.fillsketch.feature.drawing.model.PaletteVisible
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.dev.philo.fillsketch.core.designsystem.R as DesignSystemR

@Composable
fun DrawingUiButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    enabled: Boolean = true,
    contentDescription: String? = null,
    playSoundEffect: (SoundEffect) -> Unit = {},
    onClick: () -> Unit = {}
) {

    OutlinedIconButton(
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceContainer
        ),
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContentColor = MaterialTheme.colorScheme.scrim,
            disabledContainerColor = MaterialTheme.colorScheme.scrim
        ),
        enabled = enabled,
        onClick = {
            playSoundEffect(SoundEffect.BUTTON_CLICK)
            onClick()
        }
    ) {
        Image(
            modifier = Modifier.padding(6.dp),
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun DrawingPalette(
    modifier: Modifier = Modifier,
    drawingUiState: DrawingUiState,
    colorPickerController: ColorPickerController,
    openColorPickerDialog: () -> Unit,
    updateActionType: (ActionType) -> Unit,
    updateStrokeWidth: (Float) -> Unit,
    updateMagicBrushState: () -> Unit,
    initOffset: () -> Unit,
    playSoundEffect: (SoundEffect) -> Unit = {},
) {
    var paletteVisible by remember { mutableStateOf(PaletteVisible.FULL) }
    var currentPreColorIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
    ) {
        DrawingUiButton(
            modifier = Modifier
                .padding(bottom = Paddings.large, end = Paddings.large)
                .size(30.dp)
                .align(Alignment.End),
            playSoundEffect = playSoundEffect,
            painter = if (paletteVisible == PaletteVisible.HIDE) {
                painterResource(id = DesignSystemR.drawable.ic_hide_up)
            } else {
                painterResource(id = DesignSystemR.drawable.ic_hide_down)
            },
            onClick = {
                paletteVisible = when (paletteVisible) {
                    PaletteVisible.FULL -> PaletteVisible.HALF
                    PaletteVisible.HALF -> PaletteVisible.HIDE
                    PaletteVisible.HIDE -> PaletteVisible.FULL
                }
            }
        )
        DrawingUiButton(
            modifier = Modifier
                .padding(end = Paddings.large)
                .size(30.dp)
                .align(Alignment.End),
            playSoundEffect = playSoundEffect,
            painter = painterResource(id = DesignSystemR.drawable.ic_screen),
            onClick = { initOffset() }
        )

        AnimatedVisibility(
            visible = paletteVisible != PaletteVisible.HIDE,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = paletteVisible == PaletteVisible.FULL,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                ) {
                    DrawingUiButton(
                        modifier = Modifier
                            .padding(end = Paddings.medium)
                            .size(30.dp),
                        painter = painterResource(id = DesignSystemR.drawable.ic_left),
                        playSoundEffect = playSoundEffect,
                        onClick = {
                            if (currentPreColorIndex == 0) {
                                currentPreColorIndex = presetColorList.size - 1
                            } else {
                                currentPreColorIndex -= 1
                            }
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .padding(top = Paddings.xlarge),
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(top = Paddings.large)
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = Paddings.medium)
                                .padding(top = Paddings.extra, bottom = Paddings.medium),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Paddings.medium)
                        ) {
                            AnimatedVisibility(
                                visible = paletteVisible == PaletteVisible.FULL,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(Paddings.medium)
                                ) {
                                    Row(
                                        modifier = Modifier,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f),
                                            color = Color.White,
                                            shape = RoundedCornerShape(4.dp),
                                            shadowElevation = 2.dp
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(Paddings.medium),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(modifier = Modifier) {
                                                    HsvColorPicker(
                                                        modifier = Modifier
                                                            .size(25.dp),
                                                        controller = colorPickerController,
                                                        drawDefaultWheelIndicator = false,
                                                        initialColor = drawingUiState.strokeColor
                                                    )
                                                    Box(
                                                        modifier = Modifier
                                                            .size(25.dp)
                                                            .clickable {
                                                                playSoundEffect(SoundEffect.BUTTON_CLICK)
                                                                openColorPickerDialog()
                                                            }
                                                            .background(Color.White.copy(alpha = 0.3f))
                                                    )
                                                }

                                                AlphaTile(
                                                    modifier = Modifier
                                                        .padding(top = Paddings.medium)
                                                        .size(25.dp)
                                                        .clip(RoundedCornerShape(6.dp)),
                                                    controller = colorPickerController,
                                                )
                                            }
                                        }


                                        Spacer(modifier = Modifier.size(10.dp))

                                        Surface(
                                            modifier = Modifier
                                                .weight(4f),
                                            color = Color.White,
                                            shape = RoundedCornerShape(4.dp),
                                            shadowElevation = 2.dp
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(
                                                    top = Paddings.small,
                                                    bottom = Paddings.medium
                                                ),
                                                verticalArrangement = Arrangement.spacedBy(Paddings.small),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(bottom = Paddings.medium),
                                                    horizontalArrangement = Arrangement.spacedBy(
                                                        Paddings.small
                                                    )
                                                ) {
                                                    for (i in presetColorList.indices) {
                                                        Surface(
                                                            modifier = Modifier
                                                                .size(5.dp),
                                                            color = if (currentPreColorIndex == i) {
                                                                MaterialTheme.colorScheme.primary
                                                            } else {
                                                                MaterialTheme.colorScheme.secondaryContainer
                                                            },
                                                            shape = RoundedCornerShape(50.dp),
                                                            shadowElevation = 1.dp
                                                        ) {}
                                                    }
                                                }
                                                repeat(2) { i ->
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            Paddings.medium
                                                        ),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        for (j in i * 6..i * 6 + 5) {
                                                            Surface(
                                                                modifier = Modifier
                                                                    .size(25.dp)
                                                                    .border(
                                                                        3.dp,
                                                                        presetColorList[currentPreColorIndex][j],
                                                                        shape = RoundedCornerShape(
                                                                            20.dp
                                                                        )
                                                                    )
                                                                    .let {
                                                                        if (drawingUiState.strokeColor == presetColorList[currentPreColorIndex][j]) {
                                                                            it.padding(5.dp)
                                                                        } else {
                                                                            it
                                                                        }
                                                                    }
                                                                    .clickable {
                                                                        playSoundEffect(SoundEffect.BUTTON_CLICK)
                                                                        colorPickerController.selectByColor(
                                                                            presetColorList[currentPreColorIndex][j],
                                                                            true
                                                                        )
                                                                    },
                                                                color = presetColorList[currentPreColorIndex][j],
                                                                shape = RoundedCornerShape(50.dp),
                                                                shadowElevation = 1.dp
                                                            ) {}
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    AlphaSlider(
                                        modifier = Modifier
                                            .padding(horizontal = Paddings.large)
                                            .fillMaxWidth()
                                            .height(15.dp),
                                        controller = colorPickerController,
                                        wheelRadius = 8.dp
                                    )

                                    BrightnessSlider(
                                        modifier = Modifier
                                            .padding(horizontal = Paddings.large)
                                            .fillMaxWidth()
                                            .height(15.dp),
                                        controller = colorPickerController,
                                        wheelRadius = 8.dp
                                    )
                                }
                            }


                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Paddings.large),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(20.dp),
                                    painter = painterResource(
                                        id = when (drawingUiState.actionType) {
                                            ActionType.MOVE -> DesignSystemR.drawable.ic_hand
                                            ActionType.BRUSH -> DesignSystemR.drawable.ic_brush
                                            ActionType.ERASER -> DesignSystemR.drawable.ic_erase
                                            ActionType.MAGIC_BRUSH -> DesignSystemR.drawable.ic_magic
                                        }
                                    ),
                                    contentDescription = null
                                )
                                Slider(
                                    modifier = Modifier.height(15.dp),
                                    enabled = drawingUiState.actionType != ActionType.MOVE,
                                    value = drawingUiState.strokeWidth,
                                    onValueChange = { newValue ->
                                        updateStrokeWidth(newValue)
                                    },
                                    valueRange = 5f..200f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = drawingUiState.strokeColor,
                                        activeTrackColor = drawingUiState.strokeColor,
                                        inactiveTrackColor = Color.LightGray,
                                        disabledActiveTrackColor = MaterialTheme.colorScheme.scrim,
                                        disabledInactiveTrackColor = MaterialTheme.colorScheme.scrim,
                                    )
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val offsetMoveY by animateDpAsState(
                            targetValue = if (drawingUiState.actionType == ActionType.MOVE) (-10).dp else 0.dp,
                            label = "offsetMoveY"
                        )
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .offset { IntOffset(0, offsetMoveY.roundToPx()) }
                                .clickable {
                                    playSoundEffect(SoundEffect.SELECT_DRAWING_ACTION_TYPE)
                                    updateActionType(ActionType.MOVE)
                                },
                            painter = painterResource(id = DesignSystemR.drawable.ic_hand),
                            contentDescription = null
                        )
                        val offsetBrushY by animateDpAsState(
                            targetValue = if (drawingUiState.actionType == ActionType.BRUSH) (-10).dp else 0.dp,
                            label = "offsetBrushY"
                        )
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .offset { IntOffset(0, offsetBrushY.roundToPx()) }
                                .clickable {
                                    playSoundEffect(SoundEffect.SELECT_DRAWING_ACTION_TYPE)
                                    updateActionType(ActionType.BRUSH)
                                },
                            painter = painterResource(id = DesignSystemR.drawable.ic_brush),
                            contentDescription = null
                        )
                        val offsetEraserY by animateDpAsState(
                            targetValue = if (drawingUiState.actionType == ActionType.ERASER) (-10).dp else 0.dp,
                            label = "offsetEraserY"
                        )
                        Image(
                            modifier = Modifier
                                .size(30.dp)
                                .offset { IntOffset(0, offsetEraserY.roundToPx()) }
                                .clickable {
                                    playSoundEffect(SoundEffect.SELECT_DRAWING_ACTION_TYPE)
                                    updateActionType(ActionType.ERASER)
                                },
                            painter = painterResource(id = DesignSystemR.drawable.ic_erase),
                            contentDescription = null
                        )
                        val offsetMagicBrushY by animateDpAsState(
                            targetValue = if (drawingUiState.actionType == ActionType.MAGIC_BRUSH) (-10).dp else 0.dp,
                            label = "offsetMagicBrushY"
                        )
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .offset { IntOffset(0, offsetMagicBrushY.roundToPx()) }
                                .clickable {
                                    playSoundEffect(SoundEffect.SELECT_DRAWING_ACTION_TYPE)
                                    if (drawingUiState.hasMagicBrush) {
                                        updateActionType(ActionType.MAGIC_BRUSH)
                                    } else {
                                        // 광고
                                        updateMagicBrushState()
                                    }
                                }
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = DesignSystemR.drawable.ic_magic),
                                contentDescription = null
                            )
                            if (!drawingUiState.hasMagicBrush) {
                                Image(
                                    modifier = Modifier
                                        .size(15.dp)
                                        .align(Alignment.BottomEnd),
                                    painter = painterResource(id = DesignSystemR.drawable.ic_lock),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = paletteVisible == PaletteVisible.FULL,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    DrawingUiButton(
                        modifier = Modifier
                            .padding(start = Paddings.medium)
                            .size(30.dp),
                        painter = painterResource(id = DesignSystemR.drawable.ic_right),
                        playSoundEffect = playSoundEffect,
                        onClick = {
                            if (currentPreColorIndex == presetColorList.size - 1) {
                                currentPreColorIndex = 0
                            } else {
                                currentPreColorIndex += 1
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    colorPickerController: ColorPickerController,
    currentResultBitmap: Bitmap,
    initialColor: Color,
    playSoundEffect: (SoundEffect) -> Unit = {},
) {
    FillSketchDialog(
        titleText = "Color Palette",
        playSoundEffect = playSoundEffect,
        onDismissRequest = {
            onDismiss()
        }
    ) {
        val controller = rememberColorPickerController()
        var dropperMode by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            controller.selectByColor(initialColor, false)
        }

        Box {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (dropperMode) {
                    ImageDropper(
                        modifier = Modifier
                            .size(350.dp)
                            .padding(10.dp),
                        imageBitmap = currentResultBitmap,
                        selectColor = { color ->
                            controller.selectByColor(color, true)
                            colorPickerController.selectByColor(color, true)
                        }
                    )
                } else {
                    HsvColorPicker(
                        modifier = Modifier
                            .size(250.dp)
                            .padding(10.dp),
                        controller = controller,
                        initialColor = initialColor,
                        onColorChanged = { colorEnvelope ->
                            colorPickerController.selectByColor(colorEnvelope.color, true)
                        }
                    )
                }
                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp),
                    controller = controller,
                )

                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp),
                    controller = controller,
                )

                AlphaTile(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    controller = controller,
                )
            }

            if (dropperMode) {
                Image(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .align(Alignment.BottomStart)
                        .clickable {
                            playSoundEffect(SoundEffect.BUTTON_CLICK)
                            dropperMode = false
                        },
                    painter = painterResource(id = DesignSystemR.drawable.ic_colorpicker),
                    contentDescription = null
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(35.dp)
                        .align(Alignment.BottomStart)
                        .clickable {
                            playSoundEffect(SoundEffect.BUTTON_CLICK)
                            dropperMode = true
                        },
                    painter = painterResource(id = DesignSystemR.drawable.ic_dropper),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ImageDropper(
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap,
    selectColor: (Color) -> Unit,
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .onGloballyPositioned { coordinates ->
                    canvasSize = coordinates.size
                }
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val start = awaitFirstDown()
                        offset = start.position
                        val startOffset = Offset(
                            imageBitmap.width * start.position.x / canvasSize.width,
                            imageBitmap.height * start.position.y / canvasSize.height
                        )
                        selectColor(
                            Color(
                                imageBitmap.getPixel(
                                    startOffset.x
                                        .toInt()
                                        .coerceIn(0, imageBitmap.width - 1),
                                    startOffset.y
                                        .toInt()
                                        .coerceIn(0, imageBitmap.height - 1)
                                )
                            )
                        )

                        drag(start.id) { change ->
                            val calculatedOffset = Offset(
                                imageBitmap.width * change.position.x / canvasSize.width,
                                imageBitmap.height * change.position.y / canvasSize.height
                            )
                            offset = change.position
                            change.consume()
                            selectColor(
                                Color(
                                    imageBitmap.getPixel(
                                        calculatedOffset.x
                                            .toInt()
                                            .coerceIn(0, imageBitmap.width - 1),
                                        calculatedOffset.y
                                            .toInt()
                                            .coerceIn(0, imageBitmap.height - 1)
                                    )
                                )
                            )
                        }
                    }
                },
            bitmap = imageBitmap.asImageBitmap(),
            contentDescription = null
        )

        val density = LocalDensity.current
        offset = Offset(canvasSize.width.toFloat() / 2f, canvasSize.height.toFloat() / 2f)
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .align(Alignment.Center)
                .size(
                    with(density) { canvasSize.width.toDp() },
                    with(density) { canvasSize.height.toDp() }
                )
        ) {
            drawCircle(
                color = Color.White,
                radius = 12.dp.toPx(),
                center = Offset(
                    offset.x.coerceIn(0f, canvasSize.width.toFloat()),
                    offset.y.coerceIn(0f, canvasSize.height.toFloat())
                )
            )
        }
    }
}

@Composable
@Preview
fun ImageDropperPreview() {
    FillSketchTheme {
        ImageDropper(
            imageBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                .asAndroidBitmap(),
            selectColor = {}
        )
    }
}

@Preview
@Composable
fun DrawingPalettePreview() {
    FillSketchTheme {
        val colorPickerController = rememberColorPickerController()
        LaunchedEffect(Unit) {
            colorPickerController.selectByColor(Color(0xFFFDECE2), false)
            colorPickerController.enabled = false
        }
        DrawingPalette(
            drawingUiState = DrawingUiState(
                strokeWidth = 20f,
                strokeColor = Color(0xFFFDECE2),
                actionType = ActionType.BRUSH
            ),
            colorPickerController = colorPickerController,
            openColorPickerDialog = {},
            updateActionType = {},
            updateStrokeWidth = {},
            updateMagicBrushState = {},
            initOffset = {}
        )
    }
}

@Preview
@Composable
fun ColorPickerDialogPreview() {
    FillSketchTheme {
        val colorPickerController = rememberColorPickerController()
        ColorPickerDialog(
            onDismiss = { },
            colorPickerController = colorPickerController,
            currentResultBitmap = ImageBitmap.imageResource(id = SketchResource.sketchRecommendResourceIds[0])
                .asAndroidBitmap(),
            initialColor = Color.Red
        )
    }
}

@Preview
@Composable
fun DrawingUiButton1Preview() {
    FillSketchTheme {
        DrawingUiButton(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = DesignSystemR.drawable.ic_left)
        )
    }
}

@Preview
@Composable
fun DrawingUiButton2Preview() {
    FillSketchTheme {
        DrawingUiButton(
            modifier = Modifier
                .height(30.dp)
                .width(60.dp),
            painter = painterResource(id = DesignSystemR.drawable.ic_trash)
        )
    }
}

@Preview
@Composable
fun DrawingUiButton3Preview() {
    FillSketchTheme {
        DrawingUiButton(
            modifier = Modifier
                .height(30.dp)
                .width(60.dp),
            enabled = false,
            painter = painterResource(id = DesignSystemR.drawable.ic_trash)
        )
    }
}