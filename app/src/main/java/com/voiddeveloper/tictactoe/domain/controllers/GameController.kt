package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import kotlinx.coroutines.flow.SharedFlow

interface GameController {
    val gameStatus: SharedFlow<GameStatus>
    fun getGameBoard(): List<List<Cell>>
    fun getCurrentPlayer(): Player
    suspend fun addMove(coordinate: Coordinate)

    suspend fun clearBoard()
}