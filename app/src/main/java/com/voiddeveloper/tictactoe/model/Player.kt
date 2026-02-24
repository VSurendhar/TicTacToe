package com.voiddeveloper.tictactoe.model

import com.voiddeveloper.tictactoe.R
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val type: PlayerType,
    val coin: Coin?,
    val playerName : String
)

@Serializable
enum class PlayerType {
    COMPUTER, HUMAN
}

@Serializable
enum class Coin(val coinRes: Int) { X(R.drawable.ic_x), O(R.drawable.ic_o) }
