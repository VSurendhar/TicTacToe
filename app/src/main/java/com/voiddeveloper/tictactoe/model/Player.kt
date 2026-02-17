package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val type: PlayerType,
    val coin: Coin?,
)
@Serializable
enum class PlayerType {
    COMPUTER, HUMAN
}

@Serializable
enum class Coin(char: Char) { X('X'), O('O') }
