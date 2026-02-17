package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun GameBoard(
    onCellClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) {
            Row(
                modifier = modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) {
                    GameCell(onClick = onCellClick)
                }
            }
        }
    }
}

@Composable
private fun RowScope.GameCell(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(contentAlignment = Alignment.Center) {
            // X / O later
        }
    }
}