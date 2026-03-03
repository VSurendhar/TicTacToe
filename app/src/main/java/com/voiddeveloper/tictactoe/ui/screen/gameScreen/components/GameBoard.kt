package com.voiddeveloper.tictactoe.ui.screen.gameScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.Coin
import com.voiddeveloper.tictactoe.domain.model.Coordinate
import com.voiddeveloper.tictactoe.ui.theme.winColor

@Composable
fun GameBoard(
    onCellClick: (coordinate: Coordinate) -> Unit,
    modifier: Modifier = Modifier,
    gameBoard: List<List<Cell>>,
    isWin: Boolean,
    winningCells: List<Cell>?,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { row ->
            Row(
                modifier = modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { col ->
                    GameCell(
                        coin = gameBoard[row][col].player?.coin,
                        onClick = {
                            onCellClick(
                                Coordinate(row, col)
                            )
                        },
                        color = if (isWin && winningCells?.contains(gameBoard[row][col]) == true)
                            winColor
                        else
                            MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.GameCell(
    onClick: () -> Unit,
    coin: Coin?,
    color: Color,
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        color = color
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (coin?.coinRes != null) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(coin.coinRes),
                    contentDescription = "Coin X"
                )
            }
        }
    }
}