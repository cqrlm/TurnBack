package com.example.turnback.navigaiton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.turnback.R

sealed class Screen(open val route: String, open val titleId: Int? = null) {

    sealed class BottomBarItem(
        override val route: String,
        @StringRes
        override val titleId: Int,
        @DrawableRes
        val iconId: Int
    ) : Screen(route) {

        data object Timer : BottomBarItem("timer", R.string.timer, R.drawable.ic_stopwatch)

        data object Stopwatch : BottomBarItem("stopwatch", R.string.stopwatch, R.drawable.ic_timer)
    }

    companion object {

        val START_DESTINATION: Screen = BottomBarItem.Timer
    }
}
