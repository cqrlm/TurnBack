package com.example.turnback.ui.timer.state

import kotlin.time.Duration

data class TimerScreenActions(
    val start: (Duration) -> Unit = {},
    val pause: () -> Unit = {},
    val resume: () -> Unit = {},
    val stop: () -> Unit = {}
)
