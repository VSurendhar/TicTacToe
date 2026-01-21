package com.voiddeveloper.tictactoe.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerVsPlayerDialog(
    onLocalSelected: () -> Unit,
    onRemoteSelected: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Player vs Player")
        },
        text = {
            Text("Choose how you want to play")
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLocalSelected
                ) {
                    Text("Local")
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onRemoteSelected
                ) {
                    Text("Remote")
                }
            }
        }
    )
}