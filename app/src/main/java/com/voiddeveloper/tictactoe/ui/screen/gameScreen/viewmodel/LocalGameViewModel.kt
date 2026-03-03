package com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerLocal
import com.voiddeveloper.tictactoe.domain.factory.GameControllerFactory
import com.voiddeveloper.tictactoe.domain.model.Board
import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.Coordinate
import com.voiddeveloper.tictactoe.domain.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.domain.model.GameScreenDetails
import com.voiddeveloper.tictactoe.domain.model.LocalGameStatus
import com.voiddeveloper.tictactoe.domain.model.Player
import com.voiddeveloper.tictactoe.domain.model.PlayerDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

class LocalGameViewModel(
    gameControllerFactory: GameControllerFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val controller: GameController
    private val playerDetails: PlayerDetails
    private var controllerJob: Job? = null

    private val _uiState: MutableStateFlow<GameUiState>
    val uiState: StateFlow<GameUiState> get() = _uiState
    private val mutex = Mutex()

    init {

        val gameScreenDetailsStr: String =
            savedStateHandle.get<String>("gameScreenDetailsJson") ?: throw IllegalArgumentException(
                "gameScreenDetailsJson is null"
            )

        val gameScreenDetails =
            Json.decodeFromString(GameScreenDetails.serializer(), gameScreenDetailsStr)

        controller = gameControllerFactory.create(gameScreenDetails, viewModelScope)
        playerDetails = gameScreenDetails.playerDetails

        _uiState = MutableStateFlow(
            GameUiState(
                board = controller.getGameBoard(),
                players = playerDetails.players,
                currentPlayer = controller.getCurrentPlayer(),
                status = LocalGameStatus.InProgress,
                showDifficulty = controller is SinglePlayerLocal,
                gamePlayDifficulty = if (controller is SinglePlayerLocal)
                    controller.getGamePlayDifficulty()
                else GamePlayDifficulty.DEFAULT
            )
        )

        viewModelScope.launch {
            controller.gameStatus.collect { status ->
                _uiState.update {
                    it.copy(
                        board = controller.getGameBoard(),
                        currentPlayer = controller.getCurrentPlayer(),
                        status = status,
                        gamePlayDifficulty = if (controller is SinglePlayerLocal)
                            controller.getGamePlayDifficulty()
                        else GamePlayDifficulty.DEFAULT
                    )
                }
            }
        }

        if (controller is SinglePlayerLocal) {
            launchController {
                controller.startMoveIfNeed()
            }
        }

    }

    fun onMove(coordinate: Coordinate) {
        launchController {
            controller.addMove(coordinate)
        }
    }

    fun onGamePlayDifficultyChange(difficulty: GamePlayDifficulty) {
        launchController {
            if (controller is SinglePlayerLocal) {
                controller.changeGamePlayDifficulty(difficulty)
            }
        }
        onClearBoard()
    }

    fun onClearBoard() {
        _uiState.update { it.copy(status = LocalGameStatus.InProgress) }

        launchController {
            controller.clearBoard()
        }

    }

    private fun launchController(block: suspend () -> Unit) {
        controllerJob?.cancel()
        controllerJob = viewModelScope.launch {
            mutex.withLock {
                block()
            }
        }
    }

}

data class GameUiState(
    val board: List<List<Cell>> = Board.emptyBoard,
    val players: List<Player> = emptyList(),
    val currentPlayer: Player? = null,
    val status: LocalGameStatus = LocalGameStatus.InProgress,
    val showDifficulty: Boolean = false,
    val gamePlayDifficulty: GamePlayDifficulty = GamePlayDifficulty.DEFAULT,
)
