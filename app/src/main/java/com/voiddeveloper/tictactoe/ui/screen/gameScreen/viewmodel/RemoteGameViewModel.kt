package com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel

import android.util.Log
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
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val TAG = "RemoteGameViewModel"

class RemoteGameViewModel(
    private val repo: RemoteRepository,
    private val remoteGameCommand: RemoteGameCommand,
    val gameDetailsProtoDataStoreManager: GameDetailsProtoDataStoreManager,
) : ViewModel() {

    private val _uiState: MutableStateFlow<RemoteGameUiState> = MutableStateFlow(
        RemoteGameUiState()
    )
    val uiState: StateFlow<RemoteGameUiState> get() = _uiState
    var userId: String? = null

    private val _actions: MutableSharedFlow<RemoteGameAction> = MutableSharedFlow()

    val actions: SharedFlow<RemoteGameAction> = _actions

    val json = Json {
        explicitNulls = false
        ignoreUnknownKeys = true
    }

    private var timeoutJob: Job? = null

    init {
        repo.init(remoteGameCommand)
    }

    init {
        viewModelScope.launch {
            repo.serverResponseFlow.collect { response ->
                Log.d(TAG, "Server response collected: ${response.message}")
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
                        Log.i(
                            TAG,
                            "You are connected: userId=${response.userId}, roomId=${response.roomId}"
                        )
                        val userId = response.userId ?: return@collect
                        val roomId = response.roomId ?: return@collect
                        this@RemoteGameViewModel.userId = userId
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
                        Log.i(TAG, "Opponent connected")
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

                    is RemoteGameStatus.PlayerDisconnected -> {
                        Log.i(TAG, "Player disconnected: ${response.message.assignedChar}")
                        val disconnectedCoin = response.message.assignedChar.getCoin()
                        
                        _uiState.update { state ->
                            state.copy(
                                players = state.players.filter { it.coin != disconnectedCoin },
                                status = state.status.plus(response.message)
                            )
                        }
                    }

                    is RemoteGameStatus.GameStarted -> {
                        Log.i(TAG, "Game started")
                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                isWin = false,
                            )
                        }
                    }

                    is RemoteGameStatus.Turn -> {
                        val isMyTurn = response.message.playerCoin.getCoin() ==
                                _uiState.value.players.firstOrNull { it.playerName == "You" }?.coin
                        Log.d(
                            TAG,
                            "Turn: player=${response.message.playerCoin}, isMyTurn=$isMyTurn"
                        )

                        _uiState.update {
                            it.copy(
                                currentPlayer = it.players.firstOrNull { player ->
                                    player.coin == response.message.playerCoin.getCoin()
                                },
                                isWin = false,
                                board = response.message.board.toCellBoard(it.players.first { it.playerName == "You" }.coin),
                            )
                        }

                        if (isMyTurn) {
                            startTimeoutTimer()
                        } else {
                            stopTimeoutTimer()
                        }
                    }

                    is RemoteGameStatus.Win -> {
                        Log.i(TAG, "Game won by: ${response.message.coin}")
                        stopTimeoutTimer()
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
                        Log.i(TAG, "Game tied")
                        stopTimeoutTimer()
                        _uiState.update {
                            it.copy(
                                status = it.status.plus(response.message),
                                board = response.message.board.toCellBoard(it.players.first { it.playerName == "You" }.coin),
                                currentPlayer = null,
                                isWin = false,
                            )
                        }
                    }

                    is RemoteGameStatus.RoomFull -> {
                        _actions.emit(RemoteGameAction.ShortToast("Room is full"))
                    }

                    is RemoteGameStatus.InvalidAction -> {
                        _actions.emit(RemoteGameAction.ShortToast("Invalid action"))
                    }

                    is RemoteGameStatus.InvalidCredentials -> {
                        _actions.emit(RemoteGameAction.ShortToast(response.message.message))
                    }

                    is RemoteGameStatus.AlreadyFilled -> {
                        _actions.emit(RemoteGameAction.ShortToast("Cell already occupied"))
                    }

                    is RemoteGameStatus.InvalidMove -> {
                        _actions.emit(RemoteGameAction.ShortToast("Invalid move"))
                    }

                    else -> {
                        Log.w(TAG, "Received unknown message: ${response.message}")
                    }
                }
            }
        }
    }

    private fun startTimeoutTimer() {
        Log.d(TAG, "Starting timeout timer")
        stopTimeoutTimer()
        timeoutJob = viewModelScope.launch {
            val totalTime = 25000L
            val interval = 100L
            var remainingTime = totalTime

            while (remainingTime > 0) {
                _uiState.update { it.copy(timerProgress = remainingTime.toFloat() / totalTime) }
                delay(interval)
                remainingTime -= interval
            }
            Log.d(TAG, "Timeout timer reached zero")
            _uiState.update { it.copy(timerProgress = 0f) }
            makeRandomMove()
        }
    }

    private fun stopTimeoutTimer() {
        if (timeoutJob != null) Log.d(TAG, "Stopping timeout timer")
        timeoutJob?.cancel()
        timeoutJob = null
        _uiState.update { it.copy(timerProgress = 0f) }
    }

    private fun makeRandomMove() {
        Log.d(TAG, "Making a random move")
        val currentBoard = _uiState.value.board
        val emptyCells = mutableListOf<Coordinate>()

        currentBoard.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, cell ->
                if (cell.player?.coin == null) {
                    emptyCells.add(Coordinate(rowIndex, colIndex))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val randomMove = emptyCells.random()
            Log.d(TAG, "Random move selected: $randomMove")
            setMove(randomMove)
        } else {
            Log.e(TAG, "No empty cells found for random move!")
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
        if (repo.isClosed) {
            Log.w(TAG, "Attempted to set move while repository is closed")
            viewModelScope.launch {
                Log.d(TAG, "Emitting ShowReconnectionDialog action")
                _actions.emit(RemoteGameAction.ShowReconnectionDialog)
            }
        } else {
            Log.d(TAG, "Setting move at $coordinate")
            stopTimeoutTimer()
            val move = ClientMessage(
                move = GridPosition(
                    row = coordinate.row,
                    col = coordinate.col
                ),
            )
            repo.sendMessage(json.encodeToString(move))
        }
    }

    fun onRefreshBoard() {
        Log.d(TAG, "Refreshing board")
        stopTimeoutTimer()
        val move = ClientMessage(
            clearGame = true
        )
        repo.sendMessage(json.encodeToString(move))
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared, stopping timer")
        stopTimeoutTimer()
    }

    fun tryReconnecting(){

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
    val timerProgress: Float = 0f,
)

sealed interface RemoteGameAction {
    object ShowReconnectionDialog : RemoteGameAction
    object Connected : RemoteGameAction
    data class ShortToast(val message: String) : RemoteGameAction
}
