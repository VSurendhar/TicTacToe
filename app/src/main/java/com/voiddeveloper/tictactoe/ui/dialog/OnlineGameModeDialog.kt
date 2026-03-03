package com.voiddeveloper.tictactoe.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun OnlineGameModeDialog(
    onCreateRoom: () -> Unit,
    onJoinRoom: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var roomCode by remember { mutableStateOf("") }
    var showJoinSection by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Online")
        },
        text = {
            Text("Select Game Mode")
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Create Room Button
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCreateRoom
                ) {
                    Text("Create Room")
                }

                // Join Room Toggle Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = { showJoinSection = !showJoinSection }) {
                        Text(if (showJoinSection) "Cancel" else "Join Room")
                    }
                }

                // Expandable Join Section
                if (showJoinSection) {
                    OutlinedTextField(
                        value = roomCode,
                        onValueChange = { roomCode = it },
                        label = { Text("Room Code") },
                        placeholder = { Text("Enter room code...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onJoinRoom(roomCode) },
                        enabled = roomCode.isNotBlank()
                    ) {
                        Text("Join")
                    }
                }
            }
        }
    )
}