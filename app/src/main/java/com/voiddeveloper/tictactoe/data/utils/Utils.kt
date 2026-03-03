package com.voiddeveloper.tictactoe.data.utils

import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.Coin
import com.voiddeveloper.tictactoe.domain.model.Player
import com.voiddeveloper.tictactoe.domain.model.PlayerType

object Utils {

    fun String?.getCleanId(): String {
        return this?.split(".")?.firstOrNull() ?: ""
    }

    fun Char?.getCoin(): Coin? {
        return if (this == 'X') Coin.X else if(this == 'O') Coin.O else null
    }

    fun Coin?.getPlayerName(myCoin: Coin?): String {
        if (myCoin == null) {
            return "Null"
        }
        return if (myCoin == this) "You" else "Other"
    }

    fun List<List<Char?>>.toCellBoard(myCoin: Coin?): List<List<Cell>> {
        return mapIndexed { row, cols ->
            cols.mapIndexed { col, char ->
                val playerName = char.getCoin().getPlayerName(myCoin)
                Cell(
                    row = row, col = col, player = Player(
                        type = PlayerType.HUMAN, coin = char.getCoin(), playerName = playerName
                    )
                )
            }
        }
    }

}