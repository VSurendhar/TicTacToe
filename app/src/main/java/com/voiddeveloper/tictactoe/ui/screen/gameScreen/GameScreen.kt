package com.voiddeveloper.tictactoe.ui.screen.gameScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.ui.dialog.DifficultyDialog
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameBoard
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameToolbar
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.PlayerIndicator
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel.GameViewModel
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameScreen() {

    var showDifficultyDialog by remember { mutableStateOf(false) }
    val viewModel: GameViewModel = koinViewModel()
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
                },
                onReplayClick = {
                    viewModel.onClearBoard()
                },
            )

            PlayerIndicator(
                currentPlayer = state.currentPlayer,
                playerList = state.players
            )

            GameBoard(
                modifier = Modifier.fillMaxSize(),
                onCellClick = { coordinate ->
                    if (state.status is GameStatus.InProgress && state.board[coordinate.row][coordinate.col].player == null) {
                        viewModel.onMove(coordinate)
                    }
                },
                gameBoard = state.board,
                isWin = state.status is GameStatus.Won,
                winningCells = (state.status as? GameStatus.Won)?.winningCells
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

@Preview
@Composable
fun PreviewGameScreen() {
    TicTacToeTheme {
        GameScreen()
    }
}
