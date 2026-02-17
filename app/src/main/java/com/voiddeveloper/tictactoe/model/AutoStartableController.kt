package com.voiddeveloper.tictactoe.model

import kotlinx.coroutines.flow.Flow

interface AutoStartableController {
    fun startGame(): Flow<GameStatus>
}
