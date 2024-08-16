package com.example.turnback.ui.timer.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.architecture.ScreenState
import com.example.data.model.TimerPreset
import com.example.timer.state.TimerState
import com.example.timerpreset.TimerEditMode
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimerScreenState(
    val timerState: TimerState = TimerState.STOP,
    val timerDuration: Duration = 0.seconds,
    val timerPresets: SnapshotStateList<TimerPreset> = mutableStateListOf(),
    val timerEditMode: TimerEditMode = TimerEditMode.Idle
) : ScreenState
