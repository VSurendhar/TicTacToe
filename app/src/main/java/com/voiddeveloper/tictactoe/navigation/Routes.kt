package com.voiddeveloper.tictactoe.navigation

import kotlinx.serialization.Serializable


@Serializable
data object MainScreen

@Serializable
data class LocalGameScreeRoute(
    val gameScreenDetailsJson: String
)

@Serializable
data class RemoteGameScreeRoute(
    val remoteGameCommand: String
)
