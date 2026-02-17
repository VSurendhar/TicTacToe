package com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel

import androidx.lifecycle.ViewModel
import com.voiddeveloper.tictactoe.model.GameStrategy
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.util.GameController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GameViewModel : ViewModel() {

    private var gameController: GameController = GameController()

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState


    fun setGameStrategy(strategy: GameStrategy) {
        gameController.setGameStrategy(strategy)
    }

    fun getGameStrategyType(): Class<out GameStrategy> {
        return gameController.getGameStrategyType()
    }

    fun handleIntent() {

    }

}

data class UiState(val gameStrategyType: Class<out GameStrategy>? = null)