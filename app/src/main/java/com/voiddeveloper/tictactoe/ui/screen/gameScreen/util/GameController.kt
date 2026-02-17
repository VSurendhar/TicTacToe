package com.voiddeveloper.tictactoe.ui.screen.gameScreen.util

import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameState
import com.voiddeveloper.tictactoe.model.GameStrategy
import com.voiddeveloper.tictactoe.model.Move
import com.voiddeveloper.tictactoe.model.Player

class GameController {

    private val board = List(3) { MutableList<Player?>(3) { null } }
    private var availablePlayers: List<Player> = emptyList()
    private var playerQueue = ArrayDeque<Player>()
    private lateinit var gameStrategy: GameStrategy

    fun setGameStrategy(strategy: GameStrategy) {
        gameStrategy = strategy
    }

    fun setPlayers(players: List<Player>) {
        availablePlayers = players
        playerQueue = ArrayDeque(players)
    }

    fun play(): Move? {
        val move = gameStrategy.play(playerQueue.first(), board.snapShot())
        togglePlayers()
        return move
    }

    fun togglePlayers() {
        playerQueue.addLast(playerQueue.removeFirst())
    }

    fun getGameStrategyType(): Class<out GameStrategy> {
        return gameStrategy::class.java
    }

    fun getGameState(): GameState {
        return GameState(
            board = board.snapShot(),
            turn = playerQueue.first(),
            winner = null,
            isDraw = isDraw(),
            playerList = availablePlayers
        )
    }

    fun isDraw(): Boolean {
        return false
    }

    fun isWin(): Coordinate? {
        return null
    }

    fun List<MutableList<Player?>>.snapShot(): List<List<Player?>> {
        return board.map { it.toList() }
    }

}

