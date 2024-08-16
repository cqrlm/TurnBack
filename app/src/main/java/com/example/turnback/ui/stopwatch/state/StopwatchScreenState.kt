package com.example.turnback.ui.stopwatch.state

import com.example.architecture.ScreenState
import com.example.stopwatch.StopwatchState
import kotlin.time.Duration

data class StopwatchScreenState(
    val time: Duration = Duration.ZERO,
    val stopwatchState: StopwatchState = StopwatchState.STOP
) : ScreenState
