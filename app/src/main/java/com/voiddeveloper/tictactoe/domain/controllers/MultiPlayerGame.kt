package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.domain.model.Board
import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.Coordinate
import com.voiddeveloper.tictactoe.domain.model.LocalGameStatus
import com.voiddeveloper.tictactoe.domain.model.Player
import com.voiddeveloper.tictactoe.domain.model.PlayerDetails
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class MultiPlayerGame(
    val playerDetails: PlayerDetails,
) : GameController {

    private val _Local_gameStatus: MutableSharedFlow<LocalGameStatus> =
        MutableSharedFlow()
    override val gameStatus: SharedFlow<LocalGameStatus> = _Local_gameStatus.asSharedFlow()

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
                LocalGameStatus.Won(winPlayer, getWinningCells(curPlayer))
            }

            isDraw -> {
                LocalGameStatus.Draw
            }

            else -> {
                togglePlayer()
                LocalGameStatus.InProgress
            }
        }

        _Local_gameStatus.emit(newState)

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
        _Local_gameStatus.emit(LocalGameStatus.InProgress)
    }

}