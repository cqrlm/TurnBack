package com.example.turnback.services.timer.preset

import com.example.turnback.model.TimerPreset

sealed class TimerEditMode {

    data object Idle : TimerEditMode()

    data class Deletion(val selectedTimerPresetsCount: Int = 0) : TimerEditMode()

    data class Editing(val editingTimerPreset: TimerPreset? = null) : TimerEditMode()
}
