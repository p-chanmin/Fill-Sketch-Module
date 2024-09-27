package com.dev.philo.fillsketch.feature.drawing.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dev.philo.fillsketch.core.designsystem.component.FillSketchDialog
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.ActionType
import com.dev.philo.fillsketch.feature.drawing.model.DrawingUiState
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
        onClick = { onClick() }
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
    updateActionType: (ActionType) -> Unit,
    updateStrokeWidth: (Float) -> Unit,
    updateMagicBrushState: () -> Unit,
) {

    var colorPaletteDialog by remember { mutableStateOf(false) }
    var currentPreColorIndex by remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        DrawingUiButton(
            modifier = Modifier
                .padding(end = Paddings.medium)
                .size(30.dp),
            painter = painterResource(id = DesignSystemR.drawable.ic_left),
            onClick = {
                if (currentPreColorIndex == 0) {
                    currentPreColorIndex = presetColorList.size - 1
                } else {
                    currentPreColorIndex -= 1
                }
            }
        )

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
                                            .clickable { colorPaletteDialog = true }
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
                                    horizontalArrangement = Arrangement.spacedBy(Paddings.small)
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
                                        horizontalArrangement = Arrangement.spacedBy(Paddings.medium),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        for (j in i * 6..i * 6 + 5) {
                                            Surface(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .border(
                                                        3.dp,
                                                        presetColorList[currentPreColorIndex][j],
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                    .let {
                                                        if (drawingUiState.strokeColor == presetColorList[currentPreColorIndex][j]) {
                                                            it.padding(5.dp)
                                                        } else {
                                                            it
                                                        }
                                                    }
                                                    .clickable {
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
                        .clickable { updateActionType(ActionType.MOVE) },
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
                        .clickable { updateActionType(ActionType.BRUSH) },
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
                        .clickable { updateActionType(ActionType.ERASER) },
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

        DrawingUiButton(
            modifier = Modifier
                .padding(start = Paddings.medium)
                .size(30.dp),
            painter = painterResource(id = DesignSystemR.drawable.ic_right),
            onClick = {
                if (currentPreColorIndex == presetColorList.size - 1) {
                    currentPreColorIndex = 0
                } else {
                    currentPreColorIndex += 1
                }
            }
        )

        if (colorPaletteDialog) {
            ColorPickerDialog(
                onDismiss = { colorPaletteDialog = false },
                colorPickerController = colorPickerController,
                initialColor = drawingUiState.strokeColor
            )
        }
    }
}

@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    colorPickerController: ColorPickerController,
    initialColor: Color,
) {
    FillSketchDialog(
        titleText = "Color Palette",
        onDismissRequest = {
            onDismiss()
        }
    ) {
        val controller = rememberColorPickerController()
        LaunchedEffect(Unit) {
            controller.selectByColor(initialColor, false)
        }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
            updateActionType = {},
            updateStrokeWidth = {},
            updateMagicBrushState = {}
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