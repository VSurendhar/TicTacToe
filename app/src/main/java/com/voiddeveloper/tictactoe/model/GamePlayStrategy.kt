package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface GamePlayStrategy  {

    @Serializable
    data class SinglePlayer(
        val singlePlayerMode: SinglePlayerMode,
    ) : GamePlayStrategy

    @Serializable
    data object MultiPlayer : GamePlayStrategy

}