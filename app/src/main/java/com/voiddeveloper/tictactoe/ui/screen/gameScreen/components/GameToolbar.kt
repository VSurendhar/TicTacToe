package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.model.GameMode

@Composable
private fun GameToolbar(
    gameMode: GameMode,
    onPauseClick: () -> Unit,
    onReplayClick: () -> Unit,
    onDifficultyClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconButton(onClick = onPauseClick) {
                Icon(Icons.Filled.Pause, contentDescription = "Pause")
            }

            IconButton(onClick = onReplayClick) {
                Icon(Icons.Default.Refresh, contentDescription = "Replay")
            }
        }

        if (gameMode is GameMode.SinglePlayer) {
            TextButton(onClick = onDifficultyClick) {
                Text("Difficulty")
            }
        }
    }
}