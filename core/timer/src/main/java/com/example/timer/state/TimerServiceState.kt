package com.example.timer.state

import kotlin.time.Duration

data class TimerServiceState(
    val timerDuration: Duration = Duration.ZERO,
    val timerState: TimerState = TimerState.STOP
)
