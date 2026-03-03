package com.voiddeveloper.tictactoe.domain.model

interface Displayable {
    fun display(): String
}

interface GameStatus

interface LocalGameStatus : GameStatus {
    data object InProgress : LocalGameStatus
    data class Won(val winner: Player, val winningCells: List<Cell>) : LocalGameStatus, Displayable {
        override fun display(): String {
            return "${winner.coin} has won"
        }
    }

    data object Draw : LocalGameStatus, Displayable {
        override fun display(): String {
            return "Game Draw"
        }
    }

}

