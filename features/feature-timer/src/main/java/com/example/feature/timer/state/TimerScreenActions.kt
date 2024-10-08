package com.example.feature.timer.state

import com.example.architecture.ScreenActions
import com.example.data.model.TimerPreset
import kotlin.time.Duration

data class TimerScreenActions(
    val start: (Duration) -> Unit = {},
    val pause: () -> Unit = {},
    val resume: () -> Unit = {},
    val stop: () -> Unit = {},
    val save: (TimerPreset) -> Unit = {},
    val update: (TimerPreset) -> Unit = {},
    val select: (TimerPreset) -> Unit = {},
    val edit: (TimerPreset) -> Unit = {},
    val startEditing: () -> Unit = {},
    val startDeletion: () -> Unit = {},
    val swap: (Int, Int) -> Unit = { _, _ -> }
) : ScreenActions
