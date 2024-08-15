package com.example.timerpreset

import com.example.data.model.TimerPreset

sealed class TimerEditMode {

    data object Idle : TimerEditMode()

    data class Deletion(val selectedTimerPresetsCount: Int = 0) : TimerEditMode()

    data class Editing(val editingTimerPreset: TimerPreset? = null) : TimerEditMode()
}
