package com.dev.philo.fillsketch.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dev.philo.fillsketch.core.designsystem.R
import com.dev.philo.fillsketch.core.designsystem.theme.FillSketchTheme
import com.dev.philo.fillsketch.core.designsystem.theme.Paddings
import com.dev.philo.fillsketch.core.model.SoundEffect

@Composable
fun FillSketchDialog(
    titleText: String = "",
    onDismissRequest: () -> Unit = {},
    playSoundEffect: (SoundEffect) -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Box(
            modifier = Modifier.padding(Paddings.xlarge)
        ) {
            Card(
                modifier = Modifier
                    .border(
                        4.dp,
                        MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(Paddings.large),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedText(
                            textModifier = Modifier.padding(start = Paddings.small),
                            text = titleText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 30.sp,
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            outlineColor = MaterialTheme.colorScheme.onTertiary,
                            outlineDrawStyle = Stroke(
                                width = 15f
                            )
                        )

                        FillSketchSettingButton(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = R.drawable.ic_close),
                            onClick = { onDismissRequest() },
                            playSoundEffect = playSoundEffect,
                        )
                    }


                    Box(
                        modifier = Modifier
                            .padding(top = Paddings.xlarge)
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun FillSketchDialogPreview() {
    FillSketchTheme {
        FillSketchDialog(
            titleText = "Select Work !",
        ) {

        }
    }
}
