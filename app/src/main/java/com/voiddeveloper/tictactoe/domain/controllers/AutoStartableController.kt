package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.model.GameStatus
import kotlinx.coroutines.flow.Flow

interface AutoStartableController {
    fun startGame(): Flow<GameStatus>
}