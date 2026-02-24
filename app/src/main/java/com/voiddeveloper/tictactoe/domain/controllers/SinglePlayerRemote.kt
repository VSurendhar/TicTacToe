package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coin
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerType
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class SinglePlayerRemote : GameController {
    override val gameStatus: StateFlow<GameStatus>
        get() = flow { emit(GameStatus.InProgress) } as StateFlow


    override fun getGameBoard(): List<List<Cell>> {
        return Board.emptyBoard
    }

    override fun getCurrentPlayer(): Player {
        return Player(PlayerType.COMPUTER, Coin.O, "dasohf")
    }

    override suspend fun addMove(coordinate: Coordinate) {}

    override suspend fun clearBoard() {}
}
