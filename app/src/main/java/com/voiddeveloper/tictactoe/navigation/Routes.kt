package com.voiddeveloper.tictactoe.navigation

import kotlinx.serialization.Serializable


@Serializable
data object MainScreen

@Serializable
data class GameScreeRoute(
    val gameScreenDetailsJson: String
)
