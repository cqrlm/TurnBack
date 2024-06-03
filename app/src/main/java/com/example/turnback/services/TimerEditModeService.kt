package com.example.turnback.services

import com.example.turnback.services.timer.TimerEditMode
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class TimerEditModeService @Inject constructor() {

    private val _timerEditModeFlow = MutableStateFlow<TimerEditMode>(TimerEditMode.Idle)
    val timerEditModeFlow = _timerEditModeFlow.asStateFlow()

    fun setTimerEditMode(timerEditMode: TimerEditMode) {
        _timerEditModeFlow.value = timerEditMode
    }
}
