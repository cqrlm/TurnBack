package com.example.turnback.ui.timer.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimerScreenState(
    val timerState: TimerState = TimerState.STOP,
    val timerDuration: Duration = 0.seconds,
    val timerPresets: SnapshotStateList<TimerPreset> = mutableStateListOf(),
    val isEditingMode: MutableState<Boolean> = mutableStateOf(false)
)
