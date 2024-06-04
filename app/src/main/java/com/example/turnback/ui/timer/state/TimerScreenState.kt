package com.example.turnback.ui.timer.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import com.example.turnback.services.timer.preset.TimerEditMode
import com.example.turnback.ui.base.ScreenState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimerScreenState(
    val timerState: TimerState = TimerState.STOP,
    val timerDuration: Duration = 0.seconds,
    val timerPresets: SnapshotStateList<TimerPreset> = mutableStateListOf(),
    val timerEditMode: TimerEditMode = TimerEditMode.Idle
): ScreenState
