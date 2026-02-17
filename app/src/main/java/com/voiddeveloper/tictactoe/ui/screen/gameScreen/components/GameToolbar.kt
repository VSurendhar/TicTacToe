package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.controllers.SimpleMultiplayerGameController
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerController

@Composable
fun GameToolbar(
    gameStrategy: Class<out GameController>,
    onReplayClick: () -> Unit,
    onDifficultyClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (gameStrategy == SinglePlayerController::class.java) {
            TextButton(onClick = onDifficultyClick) {
                Text("Difficulty")
            }
        }

        IconButton(onClick = onReplayClick) {
            Icon(Icons.Default.Refresh, contentDescription = "Replay")
        }

    }
}

@Preview
@Composable
fun PreviewGameToolbar() {
    GameToolbar(
        gameStrategy = SimpleMultiplayerGameController::class.java,
        onReplayClick = {},
        onDifficultyClick = {}
    )
}