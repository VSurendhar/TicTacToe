package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.domain.model.Player

@Composable
fun PlayerIndicator(
    currentPlayer: Player?,
    playerList: List<Player>,
    timerProgress: Float? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        playerList.forEach { player ->
            val isActive = player == currentPlayer
            val showTimer = isActive && player.playerName == "You" && (timerProgress ?: 0f) > 0f
            
            PlayerChip(
                isActive = isActive,
                player = player,
                timerProgress = if (showTimer) timerProgress else null
            )
        }
    }
}

@Composable
private fun RowScope.PlayerChip(
    isActive: Boolean,
    player: Player,
    timerProgress: Float? = null
) {
    val containerColor =
        if (isActive) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant

    val contentColor =
        if (isActive) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
        color = containerColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = player.playerName,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )

                Spacer(modifier = Modifier.height(6.dp))

                player.coin?.coinRes?.let { resId ->
                    Icon(
                        painter = painterResource(resId),
                        contentDescription = player.playerName,
                        tint = contentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (timerProgress != null) {
                Spacer(modifier = Modifier.size(12.dp))
                CircularProgressIndicator(
                    progress = timerProgress,
                    modifier = Modifier.size(28.dp),
                    color = contentColor,
                    strokeWidth = 3.dp,
                    trackColor = contentColor.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}
