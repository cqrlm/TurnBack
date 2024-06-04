package com.example.turnback.ui.timer

import androidx.lifecycle.viewModelScope
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerState
import com.example.turnback.services.timer.preset.TimerPresetManager
import com.example.turnback.ui.base.ScreenViewModel
import com.example.turnback.ui.timer.state.TimerScreenActions
import com.example.turnback.ui.timer.state.TimerScreenState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration

@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    private val timerPresetManager: TimerPresetManager,
    @Assisted val timeFlow: Flow<Duration>,
    @Assisted val timerStateFlow: Flow<TimerState>
) : ScreenViewModel<TimerScreenState, TimerScreenActions>() {

    @AssistedFactory
    interface Factory {
        fun create(timeFlow: Flow<Duration>, timerStateFlow: Flow<TimerState>): TimerViewModel
    }

    override val screenState: StateFlow<TimerScreenState> =
        combine(
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

    override val screenActions: TimerScreenActions =
        TimerScreenActions(
            save = ::save,
            update = ::update,
            select = ::select,
            edit = ::edit,
            startEditing = ::startEditing,
            startDeletion = ::startDeletion,
            swap = ::swap
        )

    private fun save(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetManager.saveToDB(timerPreset)
        }
    }

    private fun update(timerPreset: TimerPreset) {
        timerPresetManager.update(timerPreset)
    }

    private fun select(timerPreset: TimerPreset) {
        timerPresetManager.select(timerPreset)
    }

    private fun edit(timerPreset: TimerPreset) {
        timerPresetManager.edit(timerPreset)
    }

    private fun startEditing() {
        timerPresetManager.startEditing()
    }

    private fun startDeletion() {
        timerPresetManager.startDeletion()
    }

    private fun swap(fromIndex: Int, toIndex: Int) {
        timerPresetManager.swap(fromIndex, toIndex)
    }
}
