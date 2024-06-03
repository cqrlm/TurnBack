package com.example.turnback.ui.timer.state

import com.example.turnback.model.TimerPreset
import kotlin.time.Duration

data class TimerScreenActions(
    val start: (Duration) -> Unit = {},
    val pause: () -> Unit = {},
    val resume: () -> Unit = {},
    val stop: () -> Unit = {},
    val save: (TimerPreset) -> Unit = {},
    val update: (TimerPreset) -> Unit = {},
    val select: (TimerPreset) -> Unit = {},
    val unselect: (TimerPreset) -> Unit = {},
    val edit: (TimerPreset) -> Unit = {},
    val startEditing: () -> Unit = {},
    val startDeletion: () -> Unit = {}
)
