package com.example.data.model

import kotlin.time.Duration

data class TimerPreset(
    val order: Int,
    val duration: Duration,
    val id: Int = 0,
    val selected: Boolean = false
)
