package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
data class Cell(val x: Int, val y: Int, var player: Player? = null)