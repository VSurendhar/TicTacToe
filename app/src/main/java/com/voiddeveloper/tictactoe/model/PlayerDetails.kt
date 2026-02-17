package com.voiddeveloper.tictactoe.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.random.Random
import kotlin.random.nextInt

@Serializable
open class PlayerDetails(
    val players: List<Player>,
)  {
    @Transient
    private val playerQueue: ArrayDeque<Player> = ArrayDeque<Player>(players)

    open fun toggleRandomly() {
        repeat(Random.nextInt(1..2)) {
            togglePlayer()
        }
    }

    fun togglePlayer() {
        playerQueue.addLast(playerQueue.removeFirst())
    }

    fun getCurPlayer(): Player {
        return playerQueue.first()
    }

}