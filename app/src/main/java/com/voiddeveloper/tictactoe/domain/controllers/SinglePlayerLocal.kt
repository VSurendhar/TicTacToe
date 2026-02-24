package com.voiddeveloper.tictactoe.domain.controllers

import com.voiddeveloper.tictactoe.domain.ai.GameAI
import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Board.Companion.emptyBoard
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.model.LocalGameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import com.voiddeveloper.tictactoe.model.PlayerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class SinglePlayerLocal(
    private var gameAI: GameAI,
    val playerDetails: PlayerDetails,
    coroutineScope: CoroutineScope,
) : GameController {

    private val aiThinkingDelay = 500L
    private val _gameStatus: MutableSharedFlow<LocalGameStatus> = MutableSharedFlow()
    override val localGameStatus: SharedFlow<LocalGameStatus> = _gameStatus.asSharedFlow()

    private val board = Board()
    private val gameOver: Boolean
        get() = board.isAllFilled() || board.isWin(playerDetails.players.first()) != null || board.isWin(
            playerDetails.players.last()
        ) != null

    init {
        coroutineScope.launch {
            startMoveIfNeed()
        }
    }

    suspend fun startMoveIfNeed() {
        val curPlayer = getCurrentPlayer()

        if (curPlayer.type == PlayerType.COMPUTER) {
            _gameStatus.emit(AiThinking)
            delay(aiThinkingDelay)

            val cell = gameAI.play()
            val coordinate = Coordinate(cell.row, cell.col)
            addMove(coordinate)

            refreshAi(getGameBoard())
        }
    }

    fun getGamePlayDifficulty(): GamePlayDifficulty {
        return gameAI.difficulty
    }

    override suspend fun clearBoard() {
        board.clearBoard()
        val randomIndex = if (Random.nextBoolean()) 0 else 1
        playerDetails.setStartingIndex(randomIndex)
        refreshAi(board.getBoard())
        _gameStatus.emit(LocalGameStatus.InProgress)
        startMoveIfNeed()
    }

    override fun getGameBoard(): List<List<Cell>> {
        return board.getBoard()
    }

    override fun getCurrentPlayer(): Player {
        return playerDetails.getCurPlayer()
    }

    override suspend fun addMove(coordinate: Coordinate) {
        if (gameOver) {
            throw IllegalStateException("Game already finished")
        }

        if (board.isInvalidCoordinate(coordinate.row, coordinate.col)) {
            throw RuntimeException("Invalid Coordinate")
        }

        if (!board.isCellFree(coordinate.row, coordinate.col)) {
            throw RuntimeException("(${coordinate.row},${coordinate.col}) Cell already occupied")
        }

        var curPlayer = playerDetails.getCurPlayer()
        board.setPlayer(coordinate.row, coordinate.col, curPlayer)

        val winPlayer = board.isWin(curPlayer)
        val isDraw = board.isAllFilled()

        val newState = when {
            winPlayer != null -> {
                LocalGameStatus.Won(winPlayer, board.getWinningCells(curPlayer))
            }

            isDraw -> {
                LocalGameStatus.Draw
            }

            else -> {
                playerDetails.togglePlayer()
                curPlayer = playerDetails.getCurPlayer()
                LocalGameStatus.InProgress
            }
        }

        _gameStatus.emit(newState)

        if (newState is LocalGameStatus.InProgress && curPlayer.type == PlayerType.COMPUTER) {
            _gameStatus.emit(AiThinking)
            delay(aiThinkingDelay)

            val cell = gameAI.play()
            val nextCoordinate = Coordinate(cell.row, cell.col)

            refreshAi(board.getBoard())

            addMove(nextCoordinate)
        }

        refreshAi(board.getBoard())
    }

    fun refreshAi(newBoard: List<List<Cell>>) {
        gameAI = gameAI.newInstance(newBoard)
    }

    fun getPrintableBoard(): String {
        return board.toString()
    }

    suspend fun changeGamePlayDifficulty(gamePlayDifficulty: GamePlayDifficulty) {
        refreshAi(newBoard = emptyBoard)
        gameAI.difficulty = gamePlayDifficulty
        clearBoard()
    }

    data object AiThinking : LocalGameStatus

}
