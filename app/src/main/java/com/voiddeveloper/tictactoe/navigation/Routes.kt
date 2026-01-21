package com.voiddeveloper.tictactoe.navigation


sealed interface Routes {
    val route: String

    data object ModeSelection : Routes {
        override val route = "mode_selection"
    }

    data object GameBoard : Routes {
        override val route = "game_board"
    }
}