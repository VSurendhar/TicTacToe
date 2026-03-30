package com.voiddeveloper.tictactoe.ui.screen.gameScreen.local

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerLocal
import com.voiddeveloper.tictactoe.domain.model.Displayable
import com.voiddeveloper.tictactoe.domain.model.LocalGameStatus
import com.voiddeveloper.tictactoe.ui.dialog.DifficultyDialog
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameBoard
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameToolbar
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.PlayerIndicator
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel.LocalGameViewModel
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocalGameScreen() {

    var showDifficultyDialog by remember { mutableStateOf(false) }
    val viewModel: LocalGameViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

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

            GameToolbar(
                showDifficulty = state.showDifficulty,
                onDifficultyClick = {
                    showDifficultyDialog = true
                }
            )

            PlayerIndicator(
                currentPlayer = state.currentPlayer,
                playerList = state.players
            )

            val status = state.status

            if (status is Displayable) {

                val isAiThinking =
                    status is SinglePlayerLocal.AiThinking

                InformationSection(
                    text = status.display(),
                    progressShown = isAiThinking
                )

            } else {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            GameBoard(
                modifier = Modifier.fillMaxSize(),
                onCellClick = { coordinate ->
                    if (state.status is LocalGameStatus.InProgress && state.board[coordinate.row][coordinate.col].player == null) {
                        viewModel.onMove(coordinate)
                    }
                },
                gameBoard = state.board,
                isWin = state.status is LocalGameStatus.Won,
                winningCells = (state.status as? LocalGameStatus.Won)?.winningCells
            )

        }
    }

    if (showDifficultyDialog) {
        DifficultyDialog(selectedGamePlayDifficulty = state.gamePlayDifficulty, onSelected = {
            viewModel.onGamePlayDifficultyChange(it)
            showDifficultyDialog = false
        }, onDismiss = { showDifficultyDialog = false })
    }
}

@Composable
fun InformationSection(
    text: String,
    progressShown: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = text,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.width(18.dp))
        if (progressShown) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun PreviewLocalGameScreen() {
    TicTacToeTheme {
        LocalGameScreen()
    }
}
