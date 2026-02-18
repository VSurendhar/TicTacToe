package com.voiddeveloper.tictactoe.di

import com.voiddeveloper.tictactoe.domain.factory.DefaultGameAiFactory
import com.voiddeveloper.tictactoe.domain.factory.DefaultGameControllerFactory
import com.voiddeveloper.tictactoe.domain.factory.GameAiFactory
import com.voiddeveloper.tictactoe.domain.factory.GameControllerFactory
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel.GameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<GameControllerFactory> {
        DefaultGameControllerFactory(
            aiFactory = get()
        )
    }

    single<GameAiFactory> {
        DefaultGameAiFactory()
    }

    viewModel {
        GameViewModel(
            gameControllerFactory = get(),
            savedStateHandle = get()
        )
    }

}