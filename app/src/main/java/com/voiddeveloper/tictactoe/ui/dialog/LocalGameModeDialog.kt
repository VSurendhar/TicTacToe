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
import com.voiddeveloper.tictactoe.model.Coin
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.model.GamePlayStrategy
import com.voiddeveloper.tictactoe.model.GameScreenDetails
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.PlayerType
import com.voiddeveloper.tictactoe.model.SinglePlayerMode

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
                                        type = PlayerType.COMPUTER, coin = Coin.X
                                    ), Player(
                                        type = PlayerType.HUMAN, coin = Coin.O
                                    )
                                )
                            ), gamePlayStrategy = GamePlayStrategy.SinglePlayer(
                                singlePlayerMode = SinglePlayerMode.Local(
                                    difficulty = GamePlayDifficulty.EASY,
                                    gameAi = "SimpleGameAi"
                                )
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
                                        type = PlayerType.HUMAN, coin = Coin.X
                                    ), Player(
                                        type = PlayerType.HUMAN, coin = Coin.O
                                    )
                                )
                            ), gamePlayStrategy = GamePlayStrategy.MultiPlayer
                        )
                    )
                }) {
                Text("Player vs Player")
            }

        }
    })
}