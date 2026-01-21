package com.voiddeveloper.tictactoe.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


interface GameMode : Parcelable {
    @Parcelize
    data object SinglePlayer : GameMode

    @Parcelize
    data object MultiPlayer : GameMode
}
