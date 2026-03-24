package com.voiddeveloper.tictactoe.ui.screen.modeSelectionScreen

import android.app.UiModeManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.domain.model.GameScreenDetails
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import com.voiddeveloper.tictactoe.ui.dialog.ConnectingDialog
import com.voiddeveloper.tictactoe.ui.dialog.LocalGameModeDialog
import com.voiddeveloper.tictactoe.ui.dialog.OnlineGameModeDialog
import com.voiddeveloper.tictactoe.ui.dialog.ServerIpDialog
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ModeSelectionScreen(
    navigateToLocalGame: (GameScreenDetails) -> Unit,
    navigateToRemoteGame: (RemoteGameCommand) -> Unit,
) {
    val viewModel: ModeSelectionViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Tic Tac Toe",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onLocalClick() }
            ) {
                Text("Local")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onRemoteClick() }
            ) {
                Text("Remote")
            }
        }
    }

    if (state.showLocalDialog) {
        LocalGameModeDialog(
            navigateToGameScreen = { gameScreenDetails ->
                viewModel.onDismissLocalDialog()
                navigateToLocalGame(gameScreenDetails)
            },
            onDismiss = { viewModel.onDismissLocalDialog() }
        )
    }

    if (state.showServerIpDialog) {
        ServerIpDialog(
            onConfirm = { ip, port ->
                viewModel.onConfirmServerIp(ip, port)
            },
            onDismiss = { viewModel.onDismissServerIpDialog() },
            initialIp = state.serverIp,
            initialPort = state.serverPort
        )
    }

    if (state.showConnectingDialog) {
        ConnectingDialog()
    }

    if (state.showOnlineModeDialog) {
        OnlineGameModeDialog(
            onCreateRoom = {
                viewModel.onDismissOnlineModeDialog()
                navigateToRemoteGame(RemoteGameCommand.CreateRoom(state.serverIp, state.serverPort))
            },
            onJoinRoom = { roomCode ->
                viewModel.onDismissOnlineModeDialog()
                navigateToRemoteGame(
                    RemoteGameCommand.JoinRoom(
                        roomId = roomCode,
                        serverIp = state.serverIp,
                        serverPort = state.serverPort
                    )
                )
            },
            onDismiss = { viewModel.onDismissOnlineModeDialog() }
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = UiModeManager.MODE_NIGHT_YES)
fun ModeSelectionScreenPreview() {
    TicTacToeTheme {
        ModeSelectionScreen({}, {})
    }
}
