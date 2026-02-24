package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
data class Cell(val row: Int, val col: Int, var player: Player? = null)
