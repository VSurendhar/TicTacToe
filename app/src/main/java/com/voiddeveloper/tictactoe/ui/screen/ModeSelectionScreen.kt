package com.voiddeveloper.tictactoe.ui.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.ui.dialog.PlayerVsPlayerDialog
import com.voiddeveloper.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun ModeSelectionScreen(
    onPlayerVsPlayerSelected: () -> Unit,
    onPlayerVsComputerSelected: () -> Unit,
) {
    var showPvPDialog by remember { mutableStateOf(false) }

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
                onClick = { showPvPDialog = true }
            ) {
                Text("Player vs Player")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onPlayerVsComputerSelected
            ) {
                Text("Player vs Computer")
            }
        }
    }

    if (showPvPDialog) {
        PlayerVsPlayerDialog(
            onLocalSelected = {
                showPvPDialog = false
                onPlayerVsPlayerSelected()
            },
            onRemoteSelected = {
                showPvPDialog = false
                onPlayerVsPlayerSelected()
            },
            onDismiss = { showPvPDialog = false }
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
