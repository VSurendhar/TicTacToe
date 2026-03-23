package com.voiddeveloper.tictactoe.ui.screen.gameScreen.remote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.domain.model.Coordinate
import com.voiddeveloper.tictactoe.domain.model.Displayable
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.GameBoard
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.components.PlayerIndicator
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel.RemoteGameViewModel
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RemoteGameScreen(remoteGameCommand: RemoteGameCommand) {

    val viewModel: RemoteGameViewModel = koinViewModel(
        parameters = { parametersOf(remoteGameCommand) })

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

            TopBar(roomId = state.roomId, onRefreshClick = {
                viewModel.onRefreshBoard()
            }, enableRefresh = state.players.size == 2)

            PlayerIndicator(
                currentPlayer = state.currentPlayer, 
                playerList = state.players,
                timerProgress = state.timerProgress
            )

            val status = state.status
            val filteredList = status.filterIsInstance<Displayable>().map { it.display() }
            if (filteredList.isEmpty()) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
            } else {
                InformationSection(filteredList)
            }

            GameBoard(
                modifier = Modifier.fillMaxSize(),
                onCellClick = { coordinate ->
                    if (state.currentPlayer?.playerName == "You") {
                        viewModel.setMove(Coordinate(row = coordinate.row, col = coordinate.col))
                    }
                },
                gameBoard = state.board,
                isWin = state.isWin,
                winningCells = state.winningCells
            )

        }
    }

}

@Composable
fun InformationSection(list: List<String>) {
    val trimmedList = list.takeLast(3)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        trimmedList.forEach {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TopBar(
    roomId: String,
    modifier: Modifier = Modifier,
    enableRefresh: Boolean,
    onRefreshClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Room ID",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = roomId,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(onClick = onRefreshClick, enabled = enableRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = "Replay")
            }

        }
    }
}

@Preview
@Composable
fun PreviewRemoteGameScreen() {
    TicTacToeTheme {
        RemoteGameScreen(remoteGameCommand = RemoteGameCommand.CreateRoom)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInformationSection() {
    TicTacToeTheme {
        InformationSection(
            listOf(
                "Player1 has connected",
                "Player2 has moved",
                "Player1 has moved",
                "Player1 has won"
            )
        )
    }
}
