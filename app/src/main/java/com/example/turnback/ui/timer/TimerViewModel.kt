package com.example.turnback.ui.timer

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.AppState
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.AppStateService
import com.example.turnback.services.timer.TimerState
import com.example.turnback.services.timer.preset.TimerPresetService
import com.example.turnback.ui.timer.state.TimerScreenState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration

@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    private val timerPresetService: TimerPresetService,
    private val appStateService: AppStateService,
    @Assisted val timeFlow: Flow<Duration>,
    @Assisted val timerStateFlow: Flow<TimerState>
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(timeFlow: Flow<Duration>, timerStateFlow: Flow<TimerState>): TimerViewModel
    }

    val screenState = combine(
        timeFlow,
        timerStateFlow,
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

    fun save(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetService.saveToDB(timerPreset)
        }
    }

    fun update(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetService.updateInDB(timerPreset)
        }

        appStateService.setAppState(AppState.Idle())
    }

    fun select(timerPreset: TimerPreset) {
        timerPresetService.selectTimerPreset(timerPreset)
    }

    fun unselect(timerPreset: TimerPreset) {
        timerPresetService.unselectTimerPreset(timerPreset)
    }

    fun edit(timerPreset: TimerPreset) {
        appStateService.setAppState(AppState.Editing(timerPreset))
    }

    fun startEditing() {
        appStateService.setAppState(AppState.Editing())
    }

    fun startDeletion() {
        appStateService.setAppState(AppState.Deletion())
    }
}
