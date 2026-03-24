package com.voiddeveloper.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voiddeveloper.tictactoe.domain.model.GameScreenDetails
import com.voiddeveloper.tictactoe.domain.model.RemoteGameCommand
import com.voiddeveloper.tictactoe.ui.screen.modeSelectionScreen.ModeSelectionScreen
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.local.LocalGameScreen
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.remote.RemoteGameScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController, startDestination = MainScreen
    ) {

        composable<MainScreen> {
            ModeSelectionScreen(navigateToLocalGame = { gameDetails: GameScreenDetails ->
                val randomIndex = if (Random.nextBoolean()) 0 else 1
                val newDetails = gameDetails.copy(
                    playerDetails = gameDetails.playerDetails.copy().apply {
                        setStartingIndex(randomIndex)
                    }
                )
                val json = Json.encodeToString(newDetails)
                navController.navigate(LocalGameScreeRoute(json))
            }, navigateToRemoteGame = {
                val json = Json.encodeToString(it)
                navController.navigate(RemoteGameScreeRoute(json))
            })
        }

        composable<LocalGameScreeRoute> {
            LocalGameScreen()
        }

        composable<RemoteGameScreeRoute> {
            val remoteGameCommandStr = it.arguments?.getString("remoteGameCommand") ?: return@composable
            val remoteGameCommand = Json.decodeFromString<RemoteGameCommand>(remoteGameCommandStr)
            RemoteGameScreen(
                remoteGameCommand = remoteGameCommand,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

    }
}