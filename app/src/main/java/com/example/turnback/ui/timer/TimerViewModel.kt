package com.example.turnback.ui.timer

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.AppState
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.AppStateService
import com.example.turnback.services.timer.TimerPresetService
import com.example.turnback.services.timer.TimerService
import com.example.turnback.ui.timer.state.TimerScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerService: TimerService,
    private val timerPresetService: TimerPresetService,
    private val appStateService: AppStateService
) : ViewModel() {

    val screenState = combine(
        timerService.timeFlow,
        timerService.timerState,
        timerPresetService.timerPresetsFlow,
        appStateService.appStateFlow
    ) { time, timerState, timerPresets, appState ->
        TimerScreenState(
            timerState = timerState,
            timerDuration = time,
            timerPresets = timerPresets.toMutableStateList(),
            appState = appState
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TimerScreenState())

    fun start(duration: Duration) {
        timerService.start(duration)
    }

    fun pause() {
        timerService.pause()
    }

    fun resume() {
        timerService.resume()
    }

    fun stop() {
        timerService.stop()
    }

    fun save(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetService.saveToDB(timerPreset)
        }
    }

    fun update(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetService.updateInDB(timerPreset)
            appStateService.setAppState(AppState.Idle())
        }
    }

    fun select(timerPreset: TimerPreset) {
        timerPresetService.selectTimerPreset(timerPreset)
    }

    fun unselect(timerPreset: TimerPreset) {
        timerPresetService.unselectTimerPreset(timerPreset)
    }

    fun edit(timerPreset: TimerPreset) {
        viewModelScope.launch {
            appStateService.setAppState(AppState.Editing(timerPreset))
        }
    }

    fun finishEditing() {
        viewModelScope.launch {
            appStateService.setAppState(AppState.Idle())
        }
    }
}
