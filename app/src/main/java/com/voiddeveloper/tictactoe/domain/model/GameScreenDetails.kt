package com.voiddeveloper.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GameScreenDetails(
    val playerDetails: PlayerDetails = PlayerDetails(players = emptyList()),
    val localGamePlayStrategy: LocalGamePlayStrategy,
)