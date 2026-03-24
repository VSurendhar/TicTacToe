package com.voiddeveloper.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface RemoteGameCommand {
    @Serializable
    data class CreateRoom(val serverIp: String, val serverPort: String) : RemoteGameCommand

    @Serializable
    data class JoinRoom(val serverIp: String, val serverPort: String, val roomId: String) :
        RemoteGameCommand

    @Serializable
    data class ReconnectionAttempt(
        val roomId: String,
        val userId: String,
        val assignedChar: String,
        val serverIp: String, val serverPort: String,
    ) : RemoteGameCommand
}