package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.domain.ai.GameAI
import com.voiddeveloper.tictactoe.domain.controllers.AutoStartableController
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.PlayerType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

class SimpleSinglePlayerController(
    private var gameAI: GameAI,
    val playerDetails: PlayerDetails,
) : GameController, SinglePlayerController, AutoStartableController {

    override fun startGame(): Flow<GameStatus> = flow {
        if (getCurrentPlayer().type == PlayerType.COMPUTER) {
            emit(AiThinking)
            delay(300)
            emitAll(playByAi())
            refreshAi(simpleMultiplayerGameController.getGameBoard())
        }
    }

    private val simpleMultiplayerGameController = SimpleMultiplayerGameController(
        playerDetails,
    )

    override fun getGameBoard(): List<List<Cell>> {
        return simpleMultiplayerGameController.getGameBoard()
    }

    override fun getCurrentPlayer(): Player {
        return simpleMultiplayerGameController.playerDetails.getCurPlayer()
    }

    override fun addMove(coordinate: Coordinate): Flow<GameStatus> = flow {

        simpleMultiplayerGameController.addMove(coordinate).collect { status ->

            emit(status)

            if (status is GameStatus.InProgress && simpleMultiplayerGameController.getCurPlayer().type == PlayerType.COMPUTER) {

                emit(AiThinking)

                delay(300)

                playByAi().collect {
                    emit(it)
                }

            }
            refreshAi(simpleMultiplayerGameController.getGameBoard())
        }
    }

    fun refreshAi(newBoard: List<List<Cell>>) {
        gameAI = gameAI.newInstance(newBoard)
    }

    fun getPrintableBoard(): String {
        return simpleMultiplayerGameController.getPrintableBoard()
    }

    private fun playByAi(): Flow<GameStatus> {
        val cell = gameAI.play()
        val coordinate = Coordinate(cell.x, cell.y)
        return simpleMultiplayerGameController.addMove(coordinate)
    }

    data object AiThinking : GameStatus

}

