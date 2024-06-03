package com.example.turnback.ui.timer

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.TimerEditModeService
import com.example.turnback.services.timer.TimerEditMode
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
    private val timerEditModeService: TimerEditModeService,
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
        timerEditModeService.timerEditModeFlow
    ) { time, timerState, timerPresets, timerEditMode ->
        TimerScreenState(
            timerState = timerState,
            timerDuration = time,
            timerPresets = timerPresets.toMutableStateList(),
            timerEditMode = timerEditMode
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

        timerEditModeService.setTimerEditMode(TimerEditMode.Idle)
    }

    fun select(timerPreset: TimerPreset) {
        timerPresetService.selectTimerPreset(timerPreset)
    }

    fun unselect(timerPreset: TimerPreset) {
        timerPresetService.unselectTimerPreset(timerPreset)
    }

    fun edit(timerPreset: TimerPreset) {
        timerEditModeService.setTimerEditMode(TimerEditMode.Editing(timerPreset))
    }

    fun startEditing() {
        timerEditModeService.setTimerEditMode(TimerEditMode.Editing())
    }

    fun startDeletion() {
        timerEditModeService.setTimerEditMode(TimerEditMode.Deletion())
    }
}
