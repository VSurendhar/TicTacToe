package com.voiddeveloper.tictactoe.di

import com.voiddeveloper.tictactoe.data.RemoteRepository
import com.voiddeveloper.tictactoe.data.protodatastoremanager.GameDetailsProtoDataStoreManager
import com.voiddeveloper.tictactoe.domain.factory.DefaultGameAiFactory
import com.voiddeveloper.tictactoe.domain.factory.DefaultGameControllerFactory
import com.voiddeveloper.tictactoe.domain.factory.GameAiFactory
import com.voiddeveloper.tictactoe.domain.factory.GameControllerFactory
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel.LocalGameViewModel
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.viewmodel.RemoteGameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.koinApplication
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

    single {
        GameDetailsProtoDataStoreManager(androidContext())
    }

    viewModel {
        LocalGameViewModel(
            gameControllerFactory = get(),
            savedStateHandle = get()
        )
    }

    viewModel { (remoteGameCommand: RemoteGameCommand) ->
        RemoteGameViewModel(RemoteRepository(remoteGameCommand), get())
    }

}