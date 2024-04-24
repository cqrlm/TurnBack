package com.example.turnback.ui.timer.state

import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimerScreenState(
    val timerState: TimerState = TimerState.STOP,
    val timerDuration: Duration = 0.seconds,
    val times: List<TimerPreset> = emptyList()
)
