package com.voiddeveloper.tictactoe.domain.ai

import com.voiddeveloper.tictactoe.model.Cell


interface GameAI  {
    var gameCells: List<List<Cell>>
    fun play(): Cell
    fun newInstance(newBoard: List<List<Cell>>): GameAI
}
