package com.voiddeveloper.tictactoe.model

import com.voiddeveloper.tictactoe.domain.Utils
import com.voiddeveloper.tictactoe.domain.snapShot
import kotlinx.serialization.Serializable

@Serializable
class Board {

    private val board: List<MutableList<Cell>> = List(Utils.GAME_BOARD_SIZE) { rowIdx ->
        MutableList(Utils.GAME_BOARD_SIZE) { colIdx ->
            Cell(
                rowIdx, colIdx
            )
        }
    }


    fun getWinningCells(player: Player): List<Cell> {
        board.forEach { row ->
            if (row.all { it.player == player }) {
                return row
            }
        }

        board.first().indices.forEach { col ->
            if (board.all { it[col].player == player }) {
                return board.map { it[col] }
            }
        }

        if (board.indices.all { i -> board[i][i].player == player }) {
            return board.indices.map { i -> board[i][i] }
        }

        if (board.indices.all { i -> board[i][board.size - 1 - i].player == player }) {
            return board.indices.map { i -> board[i][board.size - 1 - i] }
        }
        return emptyList()
    }

    fun isWin(player: Player): Player? {

        val hasWon = board.any { row ->
            row.all { cell ->
                cell.player == player
            }
        } || board.indices.any { col ->
            board.all { row ->
                row[col].player == player
            }
        } || board.indices.all { i ->
            board[i][i].player == player
        } || board.indices.all { i ->
            board[i][board.size - 1 - i].player == player
        }

        return if (hasWon) player else null
    }

    fun isAllFilled(): Boolean {
        return board.all { row ->
            row.all { cell ->
                cell.player != null
            }
        }
    }

    fun isCellFree(x: Int, y: Int): Boolean {
        return board[x][y].player == null
    }

    fun getBoard(): List<List<Cell>> {
        return board.snapShot()
    }

    fun isInvalidCoordinate(x: Int, y: Int): Boolean {
        return x !in board.indices || y !in board.indices
    }

    fun setPlayer(x: Int, y: Int, player: Player) {
        board[x][y].player = player
    }

    override fun toString(): String {
        return buildString {
            appendLine()
            appendLine()
            board.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, cell ->

                    val value = cell.player?.coin ?: " "

                    append(value)

                    if (colIndex != row.lastIndex) {
                        append(" | ")
                    }
                }

                if (rowIndex != board.lastIndex) {
                    appendLine()
                    appendLine("---------")
                }
            }
            appendLine()
            appendLine()
        }
    }


    companion object {
        val emptyBoard = List(Utils.GAME_BOARD_SIZE) { row ->
            List(Utils.GAME_BOARD_SIZE) { col ->
                Cell(row, col)
            }
        }
    }

}