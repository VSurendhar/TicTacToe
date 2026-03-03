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
import com.voiddeveloper.tictactoe.domain.model.Coin
import com.voiddeveloper.tictactoe.domain.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.domain.model.GameScreenDetails
import com.voiddeveloper.tictactoe.domain.model.LocalGamePlayStrategy
import com.voiddeveloper.tictactoe.domain.model.Player
import com.voiddeveloper.tictactoe.domain.model.PlayerDetails
import com.voiddeveloper.tictactoe.domain.model.PlayerType

@Composable
fun LocalGameModeDialog(
    navigateToGameScreen: (GameScreenDetails) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text("Local")
    }, text = {
        Text("Select Game Mode")
    }, confirmButton = {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    navigateToGameScreen(
                        GameScreenDetails(
                            playerDetails = PlayerDetails(
                                players = listOf(
                                    Player(
                                        type = PlayerType.COMPUTER,
                                        coin = Coin.X,
                                        playerName = "Computer"
                                    ), Player(
                                        type = PlayerType.HUMAN,
                                        coin = Coin.O,
                                        playerName = "Human 1"
                                    )
                                )
                            ), localGamePlayStrategy = LocalGamePlayStrategy.SinglePlayer(
                                difficulty = GamePlayDifficulty.EASY,
                                gameAi = "SimpleGameAi"
                            )
                        )
                    )
                }) {
                Text("Player vs Computer")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(), onClick = {
                    navigateToGameScreen(
                        GameScreenDetails(
                            playerDetails = PlayerDetails(
                                players = listOf(
                                    Player(
                                        type = PlayerType.HUMAN,
                                        coin = Coin.X,
                                        playerName = "Human 1"
                                    ), Player(
                                        type = PlayerType.HUMAN,
                                        coin = Coin.O,
                                        playerName = "Human 2"
                                    )
                                )
                            ), localGamePlayStrategy = LocalGamePlayStrategy.MultiPlayer
                        )
                    )
                }) {
                Text("Player vs Player")
            }

        }
    })
}
