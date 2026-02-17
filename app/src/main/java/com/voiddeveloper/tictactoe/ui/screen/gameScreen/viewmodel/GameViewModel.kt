package com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voiddeveloper.tictactoe.domain.controllers.GameController
import com.voiddeveloper.tictactoe.domain.factory.DefaultGameControllerFactory
import com.voiddeveloper.tictactoe.model.AutoStartableController
import com.voiddeveloper.tictactoe.model.Board
import com.voiddeveloper.tictactoe.model.Cell
import com.voiddeveloper.tictactoe.model.Coordinate
import com.voiddeveloper.tictactoe.model.GameDetails
import com.voiddeveloper.tictactoe.model.GameStatus
import com.voiddeveloper.tictactoe.model.MultiPlayerGameDetails
import com.voiddeveloper.tictactoe.model.Player
import com.voiddeveloper.tictactoe.model.PlayerDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    gameControllerFactory: DefaultGameControllerFactory,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val controller: GameController
    private val playerDetails: PlayerDetails

    private val _uiState: MutableStateFlow<GameUiState>
    val uiState: StateFlow<GameUiState> get() = _uiState

    init {

        val gameDetails: GameDetails =
            savedStateHandle.get<GameDetails>("gameDetails") ?: MultiPlayerGameDetails

        controller = gameControllerFactory.create(gameDetails)

        playerDetails = gameDetails.playerDetails
        playerDetails.toggleRandomly()

        _uiState = MutableStateFlow(
            GameUiState(
                board = controller.getGameBoard(),
                players = playerDetails.players,
                currentPlayer = controller.getCurrentPlayer(),
                status = GameStatus.InProgress
            )
        )

        if (controller is AutoStartableController) {
            viewModelScope.launch {
                controller.startGame().collect {
                    _uiState.update {
                        it.copy(
                            board = controller.getGameBoard(),
                            currentPlayer = controller.getCurrentPlayer(),
                            status = it.status
                        )
                    }
                }
            }
        }

    }

    fun onMove(coordinate: Coordinate) {
        viewModelScope.launch {
            controller.addMove(coordinate)
                .collect { status ->
                    _uiState.update {
                        it.copy(
                            board = controller.getGameBoard(),
                            currentPlayer = controller.getCurrentPlayer(),
                            status = status
                        )
                    }
                }
        }
    }

}

data class GameUiState(
    val board: List<List<Cell>> = Board.emptyBoard,
    val players: List<Player> = emptyList(),
    val currentPlayer: Player? = null,
    val status: GameStatus = GameStatus.InProgress,
)
