package com.example.turnback.ui.stopwatch.state

import com.example.turnback.services.stopwatch.StopwatchState
import com.example.turnback.ui.base.ScreenState
import kotlin.time.Duration

data class StopwatchScreenState(
    val time: Duration = Duration.ZERO,
    val stopwatchState: StopwatchState = StopwatchState.STOP
): ScreenState
