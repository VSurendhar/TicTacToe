package com.voiddeveloper.tictactoe.ui.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.delay


@Composable
fun ReconnectingDialog(
    onDismiss: (shouldPop: Boolean) -> Unit,
    onReconnect: () -> Unit,
) {

    var progress by remember { mutableFloatStateOf(0f) }
    var secondsLeft by remember { mutableIntStateOf(15) }
    var isReconnecting by remember { mutableStateOf(false) }

    BackHandler {
        onDismiss(true)
    }

    LaunchedEffect(Unit) {
        val totalDuration = 15_000L
        val intervalMs = 50L
        var elapsed = 0L

        while (elapsed < totalDuration) {
            delay(intervalMs)
            elapsed += intervalMs
            progress = elapsed.toFloat() / totalDuration.toFloat()
            secondsLeft = ((totalDuration - elapsed) / 1000L).toInt().coerceAtLeast(0)
        }

        progress = 1f
        secondsLeft = 0
        onDismiss(true)
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Connection Lost",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Unfortunately, due to low or no internet connection, your connection to the game server has been terminated. Please click the button below to reconnect.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                // Circular progress indicator
                CircularCountdownTimer(
                    progress = progress,
                    secondsLeft = secondsLeft
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Reconnect button
                    Button(
                        onClick = {
                            isReconnecting = true
                            onReconnect()
                        },
                        enabled = !isReconnecting,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (isReconnecting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            Text(
                                text = "Reconnect",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    // Cancel button
                    TextButton(
                        onClick = { onDismiss(true) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CircularCountdownTimer(
    progress: Float,
    secondsLeft: Int,
) {
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(
                x = (size.width - diameter) / 2f,
                y = (size.height - diameter) / 2f
            )
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$secondsLeft",
                style = MaterialTheme.typography.titleLarge,
                color = textColor
            )
            Text(
                text = "sec",
                style = MaterialTheme.typography.labelSmall,
                color = secondaryTextColor
            )
        }
    }
}


@Composable
@Preview
fun previewReconnectingDialog() {
    TicTacToeTheme {
        ReconnectingDialog(
            onDismiss = {},
            onReconnect = {}
        )
    }
}
