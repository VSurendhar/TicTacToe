package com.voiddeveloper.tictactoe.domain.factory

import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.controllers.RemoteSinglePlayerGameController
import com.voiddeveloper.tictactoe.domain.controllers.SimpleMultiplayerGameController
import com.voiddeveloper.tictactoe.domain.controllers.SimpleSinglePlayerController
import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.GameDetails
import com.voiddeveloper.tictactoe.model.GamePlayStrategy
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.SinglePlayerMode

interface GameControllerFactory {
    fun create(gameDetails: GameDetails): GameController
}

class DefaultGameControllerFactory(
    private val aiFactory: GameAiFactory,
) : GameControllerFactory {

    override fun create(gameDetails: GameDetails): GameController {

        return when (val strategy = gameDetails.gamePlayStrategy) {

            is GamePlayStrategy.SinglePlayer -> {
                createSinglePlayerController(
                    strategy = strategy,
                    playerDetails = gameDetails.playerDetails,
                    aiFactory = aiFactory
                )
            }

            else -> {
                SimpleMultiplayerGameController(
                    playerDetails = gameDetails.playerDetails
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
                    controller = singlePlayerMode.singlePlayerController,
                    difficulty = difficulty,
                    board = Board()
                )

                return SimpleSinglePlayerController(
                    gameAI = gameAI, playerDetails = playerDetails
                )

            }

            is SinglePlayerMode.Remote -> {
                return RemoteSinglePlayerGameController()
            }
        }

    }

}