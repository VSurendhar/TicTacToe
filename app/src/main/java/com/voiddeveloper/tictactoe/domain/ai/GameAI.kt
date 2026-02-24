package com.voiddeveloper.tictactoe.domain.ai

import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty


interface GameAI  {
    var gameCells: List<List<Cell>>
    var difficulty : GamePlayDifficulty
    fun play(): Cell
    fun newInstance(newBoard: List<List<Cell>>): GameAI
}
