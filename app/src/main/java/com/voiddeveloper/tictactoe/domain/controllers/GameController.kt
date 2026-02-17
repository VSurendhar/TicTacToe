package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import kotlinx.coroutines.flow.Flow

interface GameController {
    fun getGameBoard(): List<List<Cell>>
    fun getCurrentPlayer(): Player

    fun addMove(coordinate: Coordinate): Flow<GameStatus>
}