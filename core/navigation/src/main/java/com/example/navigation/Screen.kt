package com.example.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.resources.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    val titleId: Int?

    @Serializable
    sealed class BottomBarItem(
        @StringRes
        override val titleId: Int,
        @DrawableRes
        val iconId: Int
    ) : Screen {

        @Serializable
        data object Timer : BottomBarItem(R.string.timer, R.drawable.ic_stopwatch)

        @Serializable
        data object Stopwatch : BottomBarItem(R.string.stopwatch, R.drawable.ic_timer)
    }

    companion object {

        val START_DESTINATION: Screen = BottomBarItem.Timer
    }
}
