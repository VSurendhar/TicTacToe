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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.model.GameStrategy
import com.voiddeveloper.tictactoe.model.PlayerToAI
import com.voiddeveloper.tictactoe.ui.dialog.DifficultyDialog
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameBoard
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameToolbar
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.PlayerIndicator
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun GameScreen(
    onExit: () -> Unit,
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
                gameStrategy = PlayerToAI::class.java,
                onDifficultyClick = {
                    showDifficultyDialog = true
                },
                onReplayClick = {
                    // reset game later
                },
            )

            PlayerIndicator(activePlayer = activePlayer)

            GameBoard(
                modifier = Modifier.fillMaxSize(), onCellClick = {
                    activePlayer = if (activePlayer == 1) 2 else 1
                })

        }
    }

    if (showDifficultyDialog) {
        DifficultyDialog(onSelected = {
            showDifficultyDialog = false
        }, onDismiss = { showDifficultyDialog = false })
    }
}

@Preview
@Composable
fun PreviewGameScreen() {
    TicTacToeTheme {
        GameScreen(
            onExit = {}
        )
    }
}
