package com.example.turnback.ui.timer

import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimerScreenState(
    val timerState: TimerState,
    val timerDuration: Duration = 0.seconds,
    val times: List<TimerPreset> = emptyList(),
    val actions: TimerScreenActions = TimerScreenActions()
)

data class TimerScreenActions(
    val start: (Duration) -> Unit = {},
    val pause: () -> Unit = {},
    val resume: () -> Unit = {},
    val stop: () -> Unit = {}
)
