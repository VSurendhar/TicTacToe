package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coin
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteSinglePlayerGameController : GameController {
    override fun getGameBoard(): List<List<Cell>> {
        return Board.emptyBoard
    }

    override fun getCurrentPlayer(): Player {
        return Player(PlayerType.COMPUTER, Coin.O)
    }

    override fun addMove(coordinate: Coordinate): Flow<GameStatus> {
        return flow { }
    }
}
