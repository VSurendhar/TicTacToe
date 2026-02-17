package com.voiddeveloper.tictactoe

import kotlinx.coroutines.flow.MutableStateFlow


abstract class GameController {

    val gameBoard: List<MutableList<Player>> = emptyList()
    val boardFlow: MutableStateFlow<List<List<Player>>> = MutableStateFlow(gameBoard.snapShot())

    private val playerQueue: ArrayDeque<Player> = ArrayDeque<Player>()

    fun addPlayer(player: Player) {}

    private fun togglePlayer() {}

    fun getCurPlayer(): Player = playerQueue.first()

    fun getBoard(): List<List<Player>> = gameBoard.snapShot()

    fun List<MutableList<Player>>.snapShot(): List<List<Player>> {
        return this.map { it.toList() }
    }

    fun addMove(move: Move) {}

}

class PersonToPersonGameController(val personToPersonGameStrategy: PersonToPersonGameStrategy) :
    GameController() {

}


class PersonToComputerController(val gamePlayDifficulty: GamePlayDifficulty) : GameController() {
}

enum class Move(val x: Int, val y: Int, val player: Player)

enum class GamePlayDifficulty {
    EASY, MEDIUM, HARD
}

enum class PersonToPersonGameStrategy {
    LOCAL, REMOTE
}

interface Player {
    val coin: Coin
}

class HumanPlayer(override val coin: Coin) : Player
class ComputerPlayer(override val coin: Coin) : Player

enum class Coin { X, O }

