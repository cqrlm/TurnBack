package com.example.turnback.ui.stopwatch

import com.example.turnback.services.stopwatch.StopwatchState
import kotlin.time.Duration

data class StopwatchScreenState(
    val time: Duration = Duration.ZERO,
    val stopwatchState: StopwatchState = StopwatchState.STOP,
    val actions: StopwatchScreenActions = StopwatchScreenActions()
)

data class StopwatchScreenActions(
    val start: () -> Unit = {},
    val pause: () -> Unit = {},
    val stop: () -> Unit = {}
)
