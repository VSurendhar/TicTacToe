package com.voiddeveloper.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface RemoteGameCommand {
    @Serializable
    data object CreateRoom : RemoteGameCommand

    @Serializable
    data class JoinRoom(val roomId: String) : RemoteGameCommand
}