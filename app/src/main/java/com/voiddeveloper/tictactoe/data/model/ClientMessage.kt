package com.voiddeveloper.tictactoe.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientMessage(val move: GridPosition? = null, val clearGame: Boolean? = null)

@Serializable
data class GridPosition(
    @SerialName("row") val row: Int,
    @SerialName("col") val col: Int,
)
