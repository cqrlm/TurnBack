package com.example.turnback.ui.timer.state

import com.example.turnback.model.TimerPreset
import kotlin.time.Duration

data class TimerScreenActions(
    val start: (Duration) -> Unit = {},
    val pause: () -> Unit = {},
    val resume: () -> Unit = {},
    val stop: () -> Unit = {},
    val save: (TimerPreset) -> Unit = {},
    val delete: (List<TimerPreset>) -> Unit = {}
)
