package com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voiddeveloper.tictactoe.data.RemoteRepository
import com.voiddeveloper.tictactoe.data.model.ClientMessage
import com.voiddeveloper.tictactoe.data.model.GridPosition
import com.voiddeveloper.tictactoe.data.model.RemoteGameStatus
import com.voiddeveloper.tictactoe.data.protodatastoremanager.GameDetailsProtoDataStoreManager
import com.voiddeveloper.tictactoe.data.utils.Utils.getCleanId
import com.voiddeveloper.tictactoe.data.utils.Utils.getCoin
import com.voiddeveloper.tictactoe.data.utils.Utils.getPlayerName
import com.voiddeveloper.tictactoe.data.utils.Utils.toCellBoard
import com.voiddeveloper.tictactoe.domain.model.Board
import com.voiddeveloper.tictactoe.domain.model.Cell
import com.voiddeveloper.tictactoe.domain.model.Coin
import com.voiddeveloper.tictactoe.domain.model.Coordinate
import com.voiddeveloper.tictactoe.domain.model.GamePlayDifficulty
import com.voiddeveloper.tictactoe.domain.model.Player
import com.voiddeveloper.tictactoe.domain.model.PlayerType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RemoteGameViewModel(
    private val repo: RemoteRepository,
    val gameDetailsProtoDataStoreManager: GameDetailsProtoDataStoreManager,
) : ViewModel() {

    private val _uiState: MutableStateFlow<RemoteGameUiState> = MutableStateFlow(
        RemoteGameUiState()
    )
    val uiState: StateFlow<RemoteGameUiState> get() = _uiState

    val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    init {
        viewModelScope.launch {
            repo.serverResponseFlow.collect { response ->
                when (response.message) {

                    is RemoteGameStatus.RoomCreated -> {
                        _uiState.update {
                            it.copy(
                                status = it.status.plus(RemoteGameStatus.RoomCreated),
                                isWin = false,
                            )
                        }
                    }

                    is RemoteGameStatus.YourConnected -> {

                        val userId = response.userId ?: return@collect
                        val roomId = response.roomId ?: return@collect
                        val assignedChar = response.assignedChar ?: return@collect
                        saveAssignedChar(assignedChar)

                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                players = response.message.players.map { playerChar ->
                                    Player(
                                        type = PlayerType.HUMAN,
                                        coin = playerChar.getCoin(),
                                        playerName = playerChar.getCoin()
                                            .getPlayerName(assignedChar.getCoin())
                                    )
                                },
                                isWin = false,
                                roomId = roomId.getCleanId()
                            )
                        }

                    }

                    is RemoteGameStatus.PlayerConnected -> {

                        val assignedChar = response.assignedChar ?: return@collect

                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                players = it.players.plus(
                                    Player(
                                        type = PlayerType.HUMAN,
                                        coin = assignedChar.getCoin(),
                                        playerName = "Other"
                                    )
                                ),
                                isWin = false,
                            )
                        }

                    }

                    is RemoteGameStatus.GameStarted -> {
                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                isWin = false,
                            )
                        }
                    }

                    is RemoteGameStatus.Turn -> {
                        _uiState.update {
                            it.copy(
                                currentPlayer = it.players.firstOrNull { player ->
                                    player.coin == response.message.playerCoin.getCoin()
                                },
                                isWin = false,
                                board = response.message.board.toCellBoard(it.players.first { it.playerName == "You" }.coin),
                            )
                        }
                    }

                    is RemoteGameStatus.Win -> {
                        val boardSnapShot =
                            response.message.board.toCellBoard(_uiState.value.players.first { it.playerName == "You" }.coin)
                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                board = boardSnapShot,
                                currentPlayer = null,
                                isWin = true,
                                winningCells = boardSnapShot.getWinningCells(response.message.coin.getCoin())
                            )
                        }
                    }

                    is RemoteGameStatus.Tie -> {
                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                board = response.message.board.toCellBoard(it.players.first { it.playerName == "You" }.coin),
                                currentPlayer = null,
                                isWin = false,
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun List<List<Cell>>.getWinningCells(coin: Coin?): List<Cell> {
        this.forEach { row ->
            if (row.all { it.player?.coin == coin }) {
                return row
            }
        }

        this.first().indices.forEach { col ->
            if (this.all { it[col].player?.coin == coin }) {
                return this.map { it[col] }
            }
        }

        if (this.indices.all { i -> this[i][i].player?.coin == coin }) {
            return this.indices.map { i -> this[i][i] }
        }

        if (this.indices.all { i -> this[i][this.size - 1 - i].player?.coin == coin }) {
            return this.indices.map { i -> this[i][this.size - 1 - i] }
        }
        return emptyList()
    }

    private fun saveAssignedChar(assignedChar: Char) {
        viewModelScope.launch {
            gameDetailsProtoDataStoreManager.updateAutoSaveUserPref { gameDetailsCacheBuilder ->
                gameDetailsCacheBuilder.setAssignedChar(assignedChar.toString())
            }
        }
    }

    fun setMove(coordinate: Coordinate) {
        val move = ClientMessage(
            move = GridPosition(
                row = coordinate.row,
                col = coordinate.col
            ),
        )
        repo.sendMessage(json.encodeToString(move))
    }

    fun onRefreshBoard() {
        val move = ClientMessage(
            clearGame = true
        )
        repo.sendMessage(json.encodeToString(move))
    }

}

data class RemoteGameUiState(
    val board: List<List<Cell>> = Board.emptyBoard,
    val players: List<Player> = emptyList(),
    val currentPlayer: Player? = null,
    val status: List<RemoteGameStatus> = listOf(RemoteGameStatus.UnSpecified),
    val showDifficulty: Boolean = false,
    val gamePlayDifficulty: GamePlayDifficulty = GamePlayDifficulty.DEFAULT,
    val roomId: String = "----",
    val isWin: Boolean = false,
    val winningCells: List<Cell> = emptyList(),
)
