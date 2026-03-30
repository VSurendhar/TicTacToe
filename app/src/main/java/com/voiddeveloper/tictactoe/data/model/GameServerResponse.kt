package com.voiddeveloper.tictactoe.data.model

import com.voiddeveloper.tictactoe.domain.model.Displayable
import com.voiddeveloper.tictactoe.domain.model.GameStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GameServerResponse(
    val userId: String? = null,
    val roomId: String? = null,
    val assignedChar: Char? = null,
    val message: RemoteGameStatus,
)

@Serializable
sealed interface RemoteGameStatus : GameStatus {

    @Serializable
    @SerialName("PLAYER_CONNECTED")
    object PlayerConnected : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "Player has connected"
        }

        override fun toString(): String {
            return "PLAYER CONNECTED"
        }
    }

    @Serializable
    @SerialName("PLAYER_DISCONNECTED")
    class PlayerDisconnected(val assignedChar: Char?) : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "Player ${assignedChar} has disconnected"
        }

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
    object RoomCreated : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "Room has created"
        }

        override fun toString(): String {
            return "ROOM CREATED"
        }
    }

    @Serializable
    @SerialName("UNSPECIFIED")
    object UnSpecified : RemoteGameStatus {
        override fun toString(): String {
            return "UNSPECIFIED"
        }
    }


    @Serializable
    @SerialName("YOU ARE CONNECTED")
    data class YourConnected(val players: List<Char>) : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "You are connected"
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
        val isForced : Boolean = false
    ) : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "$coin has won"
        }
    }

    @Serializable
    @SerialName("TIE")
    data class Tie(val board: List<List<Char?>>) : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "Game has drawn"
        }

        override fun toString(): String {
            return "TIE"
        }
    }

    @Serializable
    @SerialName("SOMETHING_WENT_WRONG")
    data class SomethingWentWrong(val message: String) : RemoteGameStatus, Displayable {
        override fun display(): String {
            return "Something Went Wrong"
        }

        override fun toString(): String {
            return "Something Went Wrong"
        }
    }

}
