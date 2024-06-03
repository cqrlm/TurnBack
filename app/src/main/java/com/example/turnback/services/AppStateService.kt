package com.example.turnback.services

import com.example.turnback.services.timer.TimerEditMode
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ActivityRetainedScoped
class AppStateService @Inject constructor() {

    private val _timerEditModeFlow = MutableStateFlow<TimerEditMode>(TimerEditMode.Idle)
    val appStateFlow = _timerEditModeFlow.asStateFlow()

    val isDeletionState: Boolean
        get() = _timerEditModeFlow.value is TimerEditMode.Deletion

    fun setAppState(timerEditMode: TimerEditMode) {
        _timerEditModeFlow.value = timerEditMode
    }
}
