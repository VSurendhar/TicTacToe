package com.voiddeveloper.tictactoe.domain.factory

import com.voiddeveloper.tictactoe.domain.ai.GameAI
import com.voiddeveloper.tictactoe.domain.ai.SimpleGameAi
import com.voiddeveloper.tictactoe.domain.controllers.SimpleSinglePlayerController
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerController
import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty

interface GameAiFactory {
    fun create(
        controller: SinglePlayerController,
        difficulty: GamePlayDifficulty,
        board: Board,
    ): GameAI
}

class DefaultGameAiFactory : GameAiFactory {

    override fun create(
        controller: SinglePlayerController,
        difficulty: GamePlayDifficulty,
        board: Board,
    ): GameAI {
        return when (controller) {
            is SimpleSinglePlayerController -> SimpleGameAi(board.getBoard(), difficulty)
            else -> SimpleGameAi(board.getBoard(), difficulty)
        }
    }

}
