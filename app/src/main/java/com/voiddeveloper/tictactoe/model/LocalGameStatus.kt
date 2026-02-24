package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Displayable {
    fun display(): String
}

interface GameStatus

interface LocalGameStatus : GameStatus {
    data object InProgress : LocalGameStatus
    data class Won(val winner: Player, val winningCells: List<Cell>) : LocalGameStatus
    data object Draw : LocalGameStatus

}

@Serializable
sealed interface RemoteGameStatus : GameStatus {

    @Serializable
    @SerialName("PLAYER_CONNECTED")
    object PlayerConnected : RemoteGameStatus {
        override fun toString(): String {
            return "PLAYER CONNECTED"
        }
    }

    @Serializable
    @SerialName("PLAYER_DISCONNECTED")
    object PlayerDisconnected : RemoteGameStatus {
        override fun toString(): String {
            return "PLAYER DISCONNECTED"
        }
    }

    @Serializable
    @SerialName("ROOM_FULL")
    object RoomFull : RemoteGameStatus {
        override fun toString(): String {
            return "ROOM FULL"
        }
    }

    @Serializable
    @SerialName("INVALID_ACTION")
    object InvalidAction : RemoteGameStatus {
        override fun toString(): String {
            return "INVALID ACTION"
        }
    }

    @Serializable
    @SerialName("INVALID_CREDENTIALS")
    data class InvalidCredentials(
        val message: String,
    ) : RemoteGameStatus

    @Serializable
    @SerialName("ROOM_CREATED")
    object RoomCreated : RemoteGameStatus {
        override fun toString(): String {
            return "ROOM CREATED"
        }
    }

    @Serializable
    @SerialName("YOU ARE CONNECTED")
    object YourConnected : RemoteGameStatus {
        override fun toString(): String {
            return "YOU ARE CONNECTED"
        }
    }


    @Serializable
    @SerialName("GAME_STARTED")
    object GameStarted : RemoteGameStatus {
        override fun toString(): String {
            return "GAME STARTED"
        }
    }


    @Serializable
    @SerialName("MOVE_ACCEPTED")
    data class MoveAccepted(
        val board: List<List<Char?>>,
    ) : RemoteGameStatus


    @Serializable
    @SerialName("ALREADY_FILLED")
    object AlreadyFilled : RemoteGameStatus {
        override fun toString(): String {
            return "ALREADY FILLED"
        }
    }

    @Serializable
    @SerialName("YOUR_TURN")
    object YourTurn : RemoteGameStatus {
        override fun toString(): String {
            return "YOUR TURN"
        }
    }

    @Serializable
    @SerialName("TURN")
    data class Turn(val playerCoin: Char?, val board: List<List<Char?>>) : RemoteGameStatus

    @Serializable
    @SerialName("INVALID_MOVE")
    object InvalidMove : RemoteGameStatus {
        override fun toString(): String {
            return "INVALID MOVE"
        }
    }

    @Serializable
    @SerialName("WIN")
    data class Win(
        val coin: Char,
        val board: List<List<Char?>>,
    ) : RemoteGameStatus

    @Serializable
    @SerialName("TIE")
    data class Tie(val board: List<List<Char?>>) : RemoteGameStatus {
        override fun toString(): String {
            return "TIE"
        }
    }

}
