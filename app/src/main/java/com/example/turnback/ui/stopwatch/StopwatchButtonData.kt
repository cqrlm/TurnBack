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
            playAction: () -> Unit,
            pauseAction: () -> Unit,
            stopAction: () -> Unit
        ): List<StopwatchButtonData> =
            listOf(
                StopwatchButtonData(
                    iconId = R.drawable.ic_play,
                    descriptionId = R.string.play,
                    onClick = playAction
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
