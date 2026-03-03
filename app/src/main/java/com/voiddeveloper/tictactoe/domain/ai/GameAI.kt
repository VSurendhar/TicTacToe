package com.voiddeveloper.tictactoe.domain.ai

import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.GamePlayDifficulty


interface GameAI  {
    var gameCells: List<List<Cell>>
    var difficulty : GamePlayDifficulty
    fun play(): Cell
    fun newInstance(newBoard: List<List<Cell>>): GameAI
}
