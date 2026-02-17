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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.voiddeveloper.tictactoe.model.GameStrategy
import com.voiddeveloper.tictactoe.model.PlayerToAI

@Composable
fun GameToolbar(
    gameStrategy: Class<out GameStrategy>,
    onReplayClick: () -> Unit,
    onDifficultyClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (gameStrategy == PlayerToAI::class.java) {
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
        gameStrategy = PlayerToAI::class.java,
        onReplayClick = {},
        onDifficultyClick = {}
    )
}