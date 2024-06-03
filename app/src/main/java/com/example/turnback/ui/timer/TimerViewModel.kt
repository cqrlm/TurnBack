package com.example.turnback.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import com.example.turnback.services.timer.preset.TimerPresetManager
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
    private val timerPresetManager: TimerPresetManager,
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
        timerPresetManager.timerPresetsFlow,
        timerPresetManager.timerEditModeFlow
    ) { time, timerState, timerPresets, timerEditMode ->
        TimerScreenState(
            timerState = timerState,
            timerDuration = time,
            timerPresets = timerPresets,
            timerEditMode = timerEditMode
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TimerScreenState())

    fun save(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetManager.saveToDB(timerPreset)
        }
    }

    fun update(timerPreset: TimerPreset) {
        timerPresetManager.update(timerPreset)
    }

    fun select(timerPreset: TimerPreset) {
        timerPresetManager.select(timerPreset)
    }

    fun edit(timerPreset: TimerPreset) {
        timerPresetManager.edit(timerPreset)
    }

    fun startEditing() {
        timerPresetManager.startEditing()
    }

    fun startDeletion() {
        timerPresetManager.startDeletion()
    }

    fun swap(fromIndex: Int, toIndex: Int) {
        timerPresetManager.swap(fromIndex, toIndex)
    }
}
