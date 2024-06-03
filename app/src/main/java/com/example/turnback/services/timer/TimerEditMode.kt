package com.example.turnback.services.timer

import com.example.turnback.model.TimerPreset

sealed class TimerEditMode {

    data object Idle : TimerEditMode()

    class Deletion(val selectedTimerPresetsCount: Int = 0) : TimerEditMode()

    class Editing(val editingTimerPreset: TimerPreset? = null) : TimerEditMode()
}
