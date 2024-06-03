package com.example.turnback.services.timer.preset

import com.example.turnback.database.entities.TimerPresetDBO
import com.example.turnback.mappers.toTimerPresetDBO
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.AppStateService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityRetainedScoped
class TimerPresetSelectorService @Inject constructor(private val appStateService: AppStateService) {

    private val _selectedTimerPresetsFlow = MutableStateFlow(emptySet<TimerPreset>())

    val selectedTimerPresetsFlow = _selectedTimerPresetsFlow
        .asStateFlow()
        .onEach { timerPresets -> appStateService.updateDeletionAppState(timerPresets.size) }

    fun select(timerPreset: TimerPreset) {
        _selectedTimerPresetsFlow.update { timerPresets ->
            timerPresets + timerPreset.copy(selected = true)
        }
    }

    fun unselect(timerPreset: TimerPreset) {
        _selectedTimerPresetsFlow.update { timerPresets ->
            timerPresets - timerPreset
        }
    }

    fun clear() {
        _selectedTimerPresetsFlow.value = emptySet()
    }

    fun convertSelectedTimerPresetsToDBOS(): List<TimerPresetDBO> =
        _selectedTimerPresetsFlow.value.map(TimerPreset::toTimerPresetDBO)
}
