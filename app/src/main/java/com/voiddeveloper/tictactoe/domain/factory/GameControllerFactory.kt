package com.voiddeveloper.tictactoe.domain.factory

import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.controllers.MultiPlayerGame
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerLocal
import com.voiddeveloper.tictactoe.domain.model.Board
import com.voiddeveloper.tictactoe.domain.model.LocalGamePlayStrategy
import com.voiddeveloper.tictactoe.domain.model.GameScreenDetails
import kotlinx.coroutines.CoroutineScope

interface GameControllerFactory {
    fun create(gameScreenDetails: GameScreenDetails, coroutineScope: CoroutineScope): GameController
}

class DefaultGameControllerFactory(
    private val aiFactory: GameAiFactory,
) : GameControllerFactory {

    override fun create(
        gameScreenDetails: GameScreenDetails,
        coroutineScope: CoroutineScope,
    ): GameController {

        return when (val strategy = gameScreenDetails.localGamePlayStrategy) {

            is LocalGamePlayStrategy.SinglePlayer -> {

                val difficulty = strategy.difficulty

                val gameAI = aiFactory.create(
                    gameAI = strategy.gameAi,
                    difficulty = difficulty,
                    board = Board()
                )

                return SinglePlayerLocal(
                    gameAI = gameAI, playerDetails = gameScreenDetails.playerDetails
                )

            }

            is LocalGamePlayStrategy.MultiPlayer -> {
                MultiPlayerGame(
                    playerDetails = gameScreenDetails.playerDetails
                )
            }
        }
    }

}