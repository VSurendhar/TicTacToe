package com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerLocal
import com.voiddeveloper.tictactoe.domain.factory.GameControllerFactory
import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.model.GameScreenDetails
import com.voiddeveloper.tictactoe.model.LocalGameStatus
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class GameViewModel(
    gameControllerFactory: GameControllerFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val controller: GameController
    private val playerDetails: PlayerDetails

    private val _uiState: MutableStateFlow<GameUiState>
    val uiState: StateFlow<GameUiState> get() = _uiState

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
            controller.localGameStatus.collect { status ->
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

    }

    fun onMove(coordinate: Coordinate) {
        viewModelScope.launch {
            controller.addMove(coordinate)
        }
    }

    fun onGamePlayDifficultyChange(gamePlayDifficulty: GamePlayDifficulty) {
        viewModelScope.launch {
            when (controller) {
                is SinglePlayerLocal -> {
                    controller.changeGamePlayDifficulty(gamePlayDifficulty)
                }
            }
        }
    }

    fun onClearBoard() {
        viewModelScope.launch {
            controller.clearBoard()
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
