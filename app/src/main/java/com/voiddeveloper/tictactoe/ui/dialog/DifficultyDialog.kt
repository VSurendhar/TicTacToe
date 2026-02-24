package com.voiddeveloper.tictactoe.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty

@Composable
fun DifficultyDialog(
    onSelected: (GamePlayDifficulty) -> Unit,
    onDismiss: () -> Unit,
    selectedGamePlayDifficulty: GamePlayDifficulty,
) {
    Dialog(onDismissRequest = onDismiss) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Select Difficulty",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                DifficultyOptionButton(
                    text = "Easy",
                    isSelected = selectedGamePlayDifficulty == GamePlayDifficulty.EASY,
                    onClick = { onSelected(GamePlayDifficulty.EASY) }
                )

                DifficultyOptionButton(
                    text = "Medium",
                    isSelected = selectedGamePlayDifficulty == GamePlayDifficulty.MEDIUM,
                    onClick = { onSelected(GamePlayDifficulty.MEDIUM) }
                )

                DifficultyOptionButton(
                    text = "Hard",
                    isSelected = selectedGamePlayDifficulty == GamePlayDifficulty.HARD,
                    onClick = { onSelected(GamePlayDifficulty.HARD) }
                )
            }
        }
    }
}

@Composable
private fun DifficultyOptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant

    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}