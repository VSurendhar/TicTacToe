package com.voiddeveloper.tictactoe.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
private fun DifficultyDialog(
    onSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Difficulty") },
        confirmButton = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onSelected("Easy") }) {
                    Text("Easy")
                }
                OutlinedButton(onClick = { onSelected("Medium") }) {
                    Text("Medium")
                }
                OutlinedButton(onClick = { onSelected("Hard") }) {
                    Text("Hard")
                }
            }
        }
    )
}