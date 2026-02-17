package com.voiddeveloper.tictactoe.model

import com.voiddeveloper.tictactoe.domain.controllers.SinglePlayerController
import kotlinx.serialization.Serializable

@Serializable
sealed interface SinglePlayerMode  {

    @Serializable
    data class Local(
        val difficulty: GamePlayDifficulty,
        val singlePlayerController: SinglePlayerController,
    ) : SinglePlayerMode

    @Serializable
    object Remote : SinglePlayerMode

}
