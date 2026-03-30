package com.voiddeveloper.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface RemoteGameCommand {
    @Serializable
    data class CreateRoom(val serverUrl: String) : RemoteGameCommand

    @Serializable
    data class JoinRoom(val serverUrl: String, val roomId: String) :
        RemoteGameCommand

}