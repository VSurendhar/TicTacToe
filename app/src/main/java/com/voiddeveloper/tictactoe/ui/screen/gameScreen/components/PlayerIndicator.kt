package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PlayerIndicator(activePlayer: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PlayerChip(
            text = "Player 1",
            isActive = activePlayer == 1
        )
        PlayerChip(
            text = "Player 2",
            isActive = activePlayer == 2
        )
    }
}

@Composable
private fun RowScope.PlayerChip(text: String, isActive: Boolean) {
    Surface(
        modifier = Modifier.weight(1f),
        color = if (isActive)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center,
            color = if (isActive)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
