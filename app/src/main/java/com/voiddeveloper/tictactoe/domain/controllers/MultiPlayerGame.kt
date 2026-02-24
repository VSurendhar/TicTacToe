package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class MultiPlayerGame(
    val playerDetails: PlayerDetails,
) : GameController {

    private val _gameStatus: MutableSharedFlow<GameStatus> =
        MutableSharedFlow()
    override val gameStatus: SharedFlow<GameStatus> = _gameStatus.asSharedFlow()

    private val board = Board()

    private val gameOver: Boolean
        get() = board.isAllFilled() || isWin(playerDetails.players.first()) != null || isWin(
            playerDetails.players.last()
        ) != null


    override fun getCurrentPlayer(): Player {
        return playerDetails.getCurPlayer()
    }

    override suspend fun addMove(coordinate: Coordinate) {

        if (gameOver) {
            println(getPrintableBoard())
            throw IllegalStateException("Game already finished")
        }

        if (board.isInvalidCoordinate(coordinate.row, coordinate.col)) {
            throw RuntimeException("Invalid Coordinate")
        }

        if (!isCellFree(coordinate.row, coordinate.col)) {
            throw RuntimeException("(${coordinate.row},${coordinate.col}) Cell already occupied")
        }

        val curPlayer = getCurPlayer()
        board.setPlayer(coordinate.row, coordinate.col, curPlayer)

        val winPlayer = isWin(curPlayer)
        val isDraw = isDraw()

        val newState = when {
            winPlayer != null -> {
                GameStatus.Won(winPlayer, getWinningCells(curPlayer))
            }

            isDraw -> {
                GameStatus.Draw
            }

            else -> {
                togglePlayer()
                GameStatus.InProgress
            }
        }

        _gameStatus.emit(newState)

    }

    private fun getWinningCells(player: Player): List<Cell> {
        return board.getWinningCells(player)
    }

    private fun isWin(player: Player): Player? {
        return board.isWin(player)
    }

    private fun isDraw(): Boolean {
        return board.isAllFilled()
    }

    private fun isCellFree(row: Int, col: Int): Boolean {
        return board.isCellFree(row, col)
    }

    fun togglePlayer() {
        playerDetails.togglePlayer()
    }

    fun getCurPlayer(): Player {
        return playerDetails.getCurPlayer()
    }

    override fun getGameBoard(): List<List<Cell>> = board.getBoard()

    fun getPrintableBoard(): String {
        return board.toString()
    }

    override suspend fun clearBoard() {
        board.clearBoard()
        val randomIndex = if (Random.nextBoolean()) 0 else 1
        playerDetails.setStartingIndex(randomIndex)
        _gameStatus.emit(GameStatus.InProgress)
    }

}