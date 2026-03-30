package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameToolbar(
    showDifficulty: Boolean,
    onDifficultyClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showDifficulty) {
            TextButton(onClick = onDifficultyClick) {
                Text("Difficulty")
            }
        }

    }
}

@Preview
@Composable
fun PreviewGameToolbar() {
    GameToolbar(
        showDifficulty = false,
        onDifficultyClick = {}
    )
}
