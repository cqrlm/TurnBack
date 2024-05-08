package com.example.turnback.model

import kotlin.time.Duration

data class TimerPreset(
    val order: Int,
    val duration: Duration,
    val selected: Boolean = false
)
