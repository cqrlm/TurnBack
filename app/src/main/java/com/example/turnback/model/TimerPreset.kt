package com.example.turnback.model

import kotlin.time.Duration

data class TimerPreset(
    val order: Int,
    val duration: Duration,
    val id: Int = 0,
    val selected: Boolean = false
) {

    companion object {

        val Undefined: TimerPreset = TimerPreset(-1, Duration.ZERO)
    }
}
