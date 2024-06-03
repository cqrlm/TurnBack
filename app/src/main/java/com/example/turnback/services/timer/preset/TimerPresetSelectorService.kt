package com.example.turnback.services.timer.preset

import com.example.turnback.database.entities.TimerPresetDBO
import com.example.turnback.mappers.toTimerPresetDBO
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.TimerEditModeService
import com.example.turnback.services.timer.TimerEditMode
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityRetainedScoped
class TimerPresetSelectorService @Inject constructor(
    private val timerEditModeService: TimerEditModeService
) {

    private val _selectedTimerPresetsFlow = MutableStateFlow(emptySet<TimerPreset>())
    val selectedTimerPresetsFlow = _selectedTimerPresetsFlow.asStateFlow()

    fun select(timerPreset: TimerPreset) {
        _selectedTimerPresetsFlow.update { timerPresets ->
            timerPresets + timerPreset.copy(selected = true)
        }

        timerEditModeService.setTimerEditMode(
            TimerEditMode.Deletion(_selectedTimerPresetsFlow.value.size)
        )
    }

    fun unselect(timerPreset: TimerPreset) {
        _selectedTimerPresetsFlow.update { timerPresets ->
            timerPresets - timerPreset
        }

        timerEditModeService.setTimerEditMode(
            TimerEditMode.Deletion(_selectedTimerPresetsFlow.value.size)
        )
    }

    fun clear() {
        _selectedTimerPresetsFlow.value = emptySet()
    }

    fun convertSelectedTimerPresetsToDBOS(): List<TimerPresetDBO> =
        _selectedTimerPresetsFlow.value.map(TimerPreset::toTimerPresetDBO)
}
