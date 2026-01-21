package com.voiddeveloper.tictactoe.ui.screen.gameScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.model.GameMode

@Composable
fun GameScreen(
    onExit: () -> Unit,
    gameMode: GameMode = GameMode.SinglePlayer
) {
    var showDifficultyDialog by remember { mutableStateOf(false) }
    var activePlayer by remember { mutableStateOf(1) } // 1 or 2

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1️⃣ Toolbar
            GameToolbar(
                gameMode = gameMode,
                onDifficultyClick = {
                    if (gameMode is GameMode.SinglePlayer) {
                        showDifficultyDialog = true
                    }
                },
                onReplayClick = {
                    // reset game later
                },
                onPauseClick = {
                    // pause later
                }
            )

            // 2️⃣ Player indicator (top)
            PlayerIndicator(activePlayer = activePlayer)

            // 3️⃣ 3x3 Grid
            GameBoard(
                onCellClick = {
                    activePlayer = if (activePlayer == 1) 2 else 1
                }
            )

            // 4️⃣ Active player footer
            ActivePlayerFooter(activePlayer = activePlayer)
        }
    }

    if (showDifficultyDialog) {
        DifficultyDialog(
            onSelected = {
                showDifficultyDialog = false
            },
            onDismiss = { showDifficultyDialog = false }
        )
    }
}