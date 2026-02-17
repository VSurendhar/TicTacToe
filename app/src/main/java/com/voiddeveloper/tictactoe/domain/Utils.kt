package com.voiddeveloper.tictactoe.domain

import com.voiddeveloper.tictactoe.model.Cell

object Utils {
    const val GAME_BOARD_SIZE = 3

}

fun List<MutableList<Cell>>.snapShot(): List<List<Cell>> {
    return this.map { it.toList() }
}

enum class SimpleGameBlankCell(
    val count: Int,
) {
    Win(1), Fork(2)
}