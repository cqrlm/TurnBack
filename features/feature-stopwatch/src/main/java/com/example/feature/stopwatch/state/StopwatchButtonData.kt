package com.example.feature.stopwatch.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.resources.R

data class StopwatchButtonData(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val descriptionId: Int,
    val onClick: () -> Unit
) {

    companion object {

        fun getAll(
            start: () -> Unit,
            pause: () -> Unit,
            stop: () -> Unit
        ): List<StopwatchButtonData> =
            listOf(
                StopwatchButtonData(
                    iconId = R.drawable.ic_start,
                    descriptionId = R.string.start,
                    onClick = start
                ),
                StopwatchButtonData(
                    iconId = R.drawable.ic_pause,
                    descriptionId = R.string.pause,
                    onClick = pause
                ),
                StopwatchButtonData(
                    iconId = R.drawable.ic_stop,
                    descriptionId = R.string.stop,
                    onClick = stop
                )
            )
    }
}
