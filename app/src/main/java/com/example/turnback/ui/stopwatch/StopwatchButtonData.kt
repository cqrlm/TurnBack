package com.example.turnback.ui.stopwatch

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.turnback.R
import com.example.turnback.services.stopwatch.StopwatchState

data class StopwatchButtonData(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val descriptionId: Int,
    val onClick: () -> Unit
) {

    companion object {

        inline fun getAll(crossinline action: (StopwatchState) -> Unit): List<StopwatchButtonData> =
            listOf(
                StopwatchButtonData(
                    iconId = R.drawable.ic_start,
                    descriptionId = R.string.start,
                    onClick = { action(StopwatchState.START) }
                ),
                StopwatchButtonData(
                    iconId = R.drawable.ic_pause,
                    descriptionId = R.string.pause,
                    onClick = { action(StopwatchState.PAUSE) }
                ),
                StopwatchButtonData(
                    iconId = R.drawable.ic_stop,
                    descriptionId = R.string.stop,
                    onClick = { action(StopwatchState.STOP) }
                )
            )
    }
}
