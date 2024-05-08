package com.example.turnback.services.timer

import com.example.turnback.database.entities.TimerPresetDBO
import com.example.turnback.mappers.toTimerPresetDBO
import com.example.turnback.model.TimerPreset
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityRetainedScoped
class TimerPresetSelectorService @Inject constructor() {

    private val _selectedTimerPresetsFlow = MutableStateFlow(emptySet<TimerPreset>())
    val selectedTimerPresetsFlow = _selectedTimerPresetsFlow.asStateFlow()

    val selectedTimerPresetsCountFlow = _selectedTimerPresetsFlow.map { it.size }

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

    suspend fun clear() {
        _selectedTimerPresetsFlow.emit(emptySet())
    }

    fun convertSelectedTimerPresetsToDBOS(): List<TimerPresetDBO> =
        _selectedTimerPresetsFlow.value.map(TimerPreset::toTimerPresetDBO)
}
