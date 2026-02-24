package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlayerDetails(
    val players: List<Player>,
    private var currentIndex: Int = 0,
) {

    fun togglePlayer() {
        currentIndex = 1 - currentIndex
    }

    fun setStartingIndex(index: Int) {
        currentIndex = index
    }

    fun getCurPlayer() = players[currentIndex]


    override fun toString(): String {
        return "PlayerDetails(players=$players)"
    }

}