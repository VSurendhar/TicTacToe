package com.voiddeveloper.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voiddeveloper.tictactoe.model.GameScreenDetails
import com.voiddeveloper.tictactoe.ui.screen.ModeSelectionScreen
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.GameScreen
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
            ModeSelectionScreen(navigateToGameScreen = { gameDetails : GameScreenDetails ->
                val randomIndex = if (Random.nextBoolean()) 0 else 1
                val newDetails = gameDetails.copy(
                    playerDetails = gameDetails.playerDetails.copy().apply {
                        setStartingIndex(randomIndex)
                    }
                )
                val json = Json.encodeToString(newDetails)
                navController.navigate(GameScreeRoute(json))
            })
        }

        composable<GameScreeRoute> {
            GameScreen()
        }

    }
}