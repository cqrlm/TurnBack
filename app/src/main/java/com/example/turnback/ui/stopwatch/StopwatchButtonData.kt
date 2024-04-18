package com.example.turnback.ui.stopwatch

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.turnback.R

data class StopwatchButtonData(
    @DrawableRes
    val iconId: Int,
    @StringRes
    val descriptionId: Int,
    val onClick: () -> Unit
) {

    companion object {

        fun getAll(
            startAction: () -> Unit,
            pauseAction: () -> Unit,
            stopAction: () -> Unit
        ): List<StopwatchButtonData> =
            listOf(
                StopwatchButtonData(
                    iconId = R.drawable.ic_start,
                    descriptionId = R.string.start,
                    onClick = startAction
                ),
                StopwatchButtonData(
                    iconId = R.drawable.ic_pause,
                    descriptionId = R.string.pause,
                    onClick = pauseAction
                ),
                StopwatchButtonData(
                    iconId = R.drawable.ic_stop,
                    descriptionId = R.string.stop,
                    onClick = stopAction
                )
            )
    }
}
