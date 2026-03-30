package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.Coordinate
import com.voiddeveloper.tictactoe.domain.model.LocalGameStatus
import com.voiddeveloper.tictactoe.domain.model.Player
import kotlinx.coroutines.flow.SharedFlow

interface GameController {
    val gameStatus: SharedFlow<LocalGameStatus>
    fun getGameBoard(): List<List<Cell>>
    fun getCurrentPlayer(): Player
    suspend fun addMove(coordinate: Coordinate)
}
