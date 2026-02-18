package com.voiddeveloper.tictactoe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class GamePlayDifficulty : Parcelable {
    EASY, MEDIUM, HARD
}
