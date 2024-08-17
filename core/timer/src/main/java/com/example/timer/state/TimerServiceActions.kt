package com.example.timer.state

import kotlin.time.Duration

data class TimerServiceActions(
    val start: (Duration) -> Unit,
    val pause: () -> Unit,
    val resume: () -> Unit,
    val stop: () -> Unit
)
