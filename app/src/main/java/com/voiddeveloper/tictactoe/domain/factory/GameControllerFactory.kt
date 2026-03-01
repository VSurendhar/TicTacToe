package com.voiddeveloper.tictactoe.domain.factory

import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerRemote
import com.voiddeveloper.tictactoe.domain.controllers.MultiPlayerGame
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerLocal
import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.GamePlayStrategy
import com.voiddeveloper.tictactoe.model.GameScreenDetails
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.SinglePlayerMode
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

        return when (val strategy = gameScreenDetails.gamePlayStrategy) {

            is GamePlayStrategy.SinglePlayer -> {
                createSinglePlayerController(
                    strategy = strategy,
                    playerDetails = gameScreenDetails.playerDetails,
                    aiFactory = aiFactory,
                )
            }

            is GamePlayStrategy.MultiPlayer -> {
                MultiPlayerGame(
                    playerDetails = gameScreenDetails.playerDetails
                )
            }
        }
    }

    private fun createSinglePlayerController(
        strategy: GamePlayStrategy.SinglePlayer,
        playerDetails: PlayerDetails,
        aiFactory: GameAiFactory,
    ): GameController {

        when (strategy.singlePlayerMode) {
            is SinglePlayerMode.Local -> {

                val difficulty = strategy.singlePlayerMode.difficulty
                val singlePlayerMode = strategy.singlePlayerMode

                val gameAI = aiFactory.create(
                    gameAI = singlePlayerMode.gameAi,
                    difficulty = difficulty,
                    board = Board()
                )

                return SinglePlayerLocal(
                    gameAI = gameAI, playerDetails = playerDetails
                )

            }

            is SinglePlayerMode.Remote -> {
                return SinglePlayerRemote()
            }
        }

    }

}