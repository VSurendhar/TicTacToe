package com.voiddeveloper.tictactoe.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class GamePlayDifficulty : Parcelable {
    EASY, MEDIUM, HARD,DEFAULT
}
