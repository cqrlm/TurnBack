package com.example.feature.timer

import androidx.lifecycle.viewModelScope
import com.example.architecture.ScreenViewModel
import com.example.data.model.TimerPreset
import com.example.feature.timer.state.TimerScreenActions
import com.example.feature.timer.state.TimerScreenState
import com.example.timerpreset.TimerPresetManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerPresetManager: TimerPresetManager,
) : ScreenViewModel<TimerScreenState, TimerScreenActions>() {

    override val screenState: StateFlow<TimerScreenState> =
        combine(
            timerPresetManager.timerPresetsFlow,
            timerPresetManager.timerEditModeFlow
        ) { timerPresets, timerEditMode ->
            TimerScreenState(
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
