package com.voiddeveloper.tictactoe.model

interface GameStatus {
    data object InProgress : GameStatus
    data class Won(val winner: Player, val winningCells: List<Cell>) : GameStatus
    data object Draw : GameStatus
}