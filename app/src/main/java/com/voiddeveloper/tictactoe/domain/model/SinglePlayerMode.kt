package com.voiddeveloper.tictactoe.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface SinglePlayerMode {

    @Serializable
    data object Local: SinglePlayerMode

}
