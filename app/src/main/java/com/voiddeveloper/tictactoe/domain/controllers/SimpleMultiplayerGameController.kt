package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SimpleMultiplayerGameController(
    val playerDetails: PlayerDetails,
) : GameController {

    private val board = Board()

    private val gameOver: Boolean
        get() = board.isAllFilled() || isWin(playerDetails.players.first()) != null || isWin(
            playerDetails.players.last()
        ) != null


    override fun getCurrentPlayer(): Player {
        return playerDetails.getCurPlayer()
    }

    override fun addMove(coordinate: Coordinate): Flow<GameStatus> {

        if (gameOver) {
            println(getPrintableBoard())
            throw IllegalStateException("Game already finished")
        }


        if (board.isInvalidCoordinate(coordinate.x, coordinate.y)) {
            throw RuntimeException("Invalid Coordinate")
        }

        if (!isCellFree(coordinate.x, coordinate.y)) {
            throw RuntimeException("(${coordinate.x},${coordinate.y}) Cell already occupied")
        }

        val curPlayer = getCurPlayer()
        board.setPlayer(coordinate.x, coordinate.y, curPlayer)

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

        return flow { emit(newState) }

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

    private fun isCellFree(x: Int, y: Int): Boolean {
        return board.isCellFree(x, y)
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

}