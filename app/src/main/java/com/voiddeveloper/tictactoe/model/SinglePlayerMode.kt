package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface SinglePlayerMode {

    @Serializable
    data class Local(
        val difficulty: GamePlayDifficulty,
        val gameAi: String,
    ) : SinglePlayerMode

    @Serializable
    data object Remote : SinglePlayerMode

}
