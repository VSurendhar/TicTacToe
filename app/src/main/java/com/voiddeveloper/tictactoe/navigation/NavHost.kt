package com.voiddeveloper.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voiddeveloper.tictactoe.ui.screen.ModeSelectionScreen
import com.voiddeveloper.tictactoe.ui.screen.gameScreen.GameScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController, startDestination = Routes.ModeSelection.route
    ) {

        composable(Routes.ModeSelection.route) {
            ModeSelectionScreen(onPlayerVsPlayerSelected = {
                navController.navigate(Routes.GameBoard.route)
            }, onPlayerVsComputerSelected = {
                navController.navigate(Routes.GameBoard.route)
            })
        }

        composable(Routes.GameBoard.route) {
            GameScreen(
                onExit = {
                    navController.popBackStack(
                        Routes.ModeSelection.route, inclusive = false
                    )
                }
            )
        }

    }
}