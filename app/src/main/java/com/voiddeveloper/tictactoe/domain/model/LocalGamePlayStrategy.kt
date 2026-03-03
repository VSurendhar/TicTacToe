package com.voiddeveloper.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface LocalGamePlayStrategy {

    @Serializable
    data class SinglePlayer(
        val difficulty: GamePlayDifficulty,
        val gameAi: String,
    ) : LocalGamePlayStrategy

    @Serializable
    data object MultiPlayer : LocalGamePlayStrategy

}