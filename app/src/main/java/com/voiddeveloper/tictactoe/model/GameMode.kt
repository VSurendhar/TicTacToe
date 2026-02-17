package com.voiddeveloper.tictactoe.model

import com.voiddeveloper.tictactoe.R

enum class Coin(res: Int) {
    X(R.drawable.ic_x), O(R.drawable.ic_o),
}

sealed interface Player {
    data class Human(val name: String, val coin: Coin) : Player
    data class AI(val coin: Coin) : Player
}

data class Move(val player: Player, val coordinate: Coordinate)

data class Coordinate(val x: Int, val y: Int)
interface GameStrategy {
    fun play(player: Player, board: List<List<Player?>>): Move?
}

class PlayerToPlayerStrategy() : GameStrategy {

    override fun play(player: Player, board: List<List<Player?>>): Move? {
        return null
    }

}

class PlayerToAI() : GameStrategy {

    override fun play(player: Player, board: List<List<Player?>>): Move? {
        return null

    }

}

class PlayerToServer() : GameStrategy {

    override fun play(player: Player, board: List<List<Player?>>): Move? {
        return null
    }

}

data class GameState(
    val board: List<List<Player?>>,
    val turn: Player,
    val winner: Player? = null,
    val isDraw: Boolean = false,
    val playerList: List<Player>,
)