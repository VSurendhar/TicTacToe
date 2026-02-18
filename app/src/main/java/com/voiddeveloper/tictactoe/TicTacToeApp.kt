package com.voiddeveloper.tictactoe

import android.app.Application
import com.voiddeveloper.tictactoe.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TicTacToeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@TicTacToeApp)
            modules(appModule)
        }

    }

}