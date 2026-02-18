package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
data class GameScreenDetails(
    val playerDetails: PlayerDetails = PlayerDetails(players = emptyList()),
    val gamePlayStrategy: GamePlayStrategy,
)