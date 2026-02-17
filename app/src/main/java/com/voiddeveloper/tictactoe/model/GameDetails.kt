package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
class GameDetails(val playerDetails: PlayerDetails, val gamePlayStrategy: GamePlayStrategy)

val MultiPlayerGameDetails = GameDetails(
    playerDetails = PlayerDetails(
        players = listOf(Player(PlayerType.HUMAN, Coin.X), Player(PlayerType.COMPUTER, Coin.O))
    ),
    gamePlayStrategy = GamePlayStrategy.MultiPlayer
)
