package com.example.turnback.services.timer.preset

import androidx.compose.runtime.mutableStateListOf
import com.example.database.entities.TimerPresetDBO
import com.example.turnback.database.repositories.TimerPresetRepository
import com.example.turnback.mappers.toTimerPreset
import com.example.turnback.mappers.toTimerPresetDBO
import com.example.turnback.model.TimerPreset
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@ActivityRetainedScoped
class TimerPresetManager @Inject constructor(
    private val timerPresetRepository: TimerPresetRepository
) {

    private val _timerEditModeFlow = MutableStateFlow<TimerEditMode>(TimerEditMode.Idle)
    val timerEditModeFlow = _timerEditModeFlow.asStateFlow()

    private val notUpdatedTimerPresets = mutableListOf<TimerPreset>()
    private val timerPresets = mutableStateListOf<TimerPreset>()

    val timerPresetsFlow = timerPresetRepository
        .getAll()
        .flowOn(Dispatchers.IO)
        .flatMapConcat { timerPresetsDBOS ->
            val databaseTimerPresets = timerPresetsDBOS.map(TimerPresetDBO::toTimerPreset)

            timerPresets.clear()
            timerPresets.addAll(databaseTimerPresets)

            flowOf(timerPresets)
        }

    suspend fun saveToDB(timerPreset: TimerPreset) {
        withContext(Dispatchers.IO) {
            timerPresetRepository.insert(timerPreset.toTimerPresetDBO())
        }
    }

    /**
     * Deletion
     */

    fun startDeletion() {
        _timerEditModeFlow.value = TimerEditMode.Deletion()
    }

    fun cancelDeletion() {
        timerPresets.replaceAll { it.copy(selected = false) }
        _timerEditModeFlow.value = TimerEditMode.Idle
    }

    fun select(timerPreset: TimerPreset) {
        val index = timerPresets.indexOf(timerPreset)
        val selected = !timerPreset.selected
        timerPresets[index] = timerPreset.copy(selected = selected)

        _timerEditModeFlow.update { timerEditMode ->
            when {
                timerEditMode is TimerEditMode.Deletion && selected ->
                    timerEditMode.copy(timerEditMode.selectedTimerPresetsCount.inc())

                timerEditMode is TimerEditMode.Deletion && !selected ->
                    timerEditMode.copy(timerEditMode.selectedTimerPresetsCount.dec())

                else -> timerEditMode
            }
        }
    }

    suspend fun deleteSelectedTimerPresets() {
        val timerPresetsDBOS = timerPresets
            .filter(TimerPreset::selected)
            .map(TimerPreset::toTimerPresetDBO)

        withContext(Dispatchers.IO) {
            timerPresetRepository.delete(*timerPresetsDBOS.toTypedArray())
        }

        _timerEditModeFlow.value = TimerEditMode.Idle
    }

    /**
     * Editing
     */

    fun startEditing() {
        _timerEditModeFlow.value = TimerEditMode.Editing()
    }

    fun cancelEditing() {
        for (timerPreset in notUpdatedTimerPresets) {
            val index = timerPresets.indexOfFirst { it.id == timerPreset.id }
            timerPresets[index] = timerPreset
        }

        timerPresets.sortBy(TimerPreset::order)

        _timerEditModeFlow.value = TimerEditMode.Idle

        notUpdatedTimerPresets.clear()
    }

    fun edit(timerPreset: TimerPreset) {
        _timerEditModeFlow.value = TimerEditMode.Editing(timerPreset)
    }

    fun update(timerPreset: TimerPreset) {
        val index = timerPresets.indexOfFirst { it.id == timerPreset.id }

        if (notUpdatedTimerPresets.all { it.id != timerPreset.id }) {
            notUpdatedTimerPresets += timerPresets[index]
        }

        timerPresets[index] = timerPreset
    }

    suspend fun saveEditingChanges() {
        val timerPresetsDBOS = timerPresets
            .mapIndexed { index, timerPreset -> timerPreset.copy(order = index) }
            .map(TimerPreset::toTimerPresetDBO)

        withContext(Dispatchers.IO) {
            timerPresetRepository.update(
                *timerPresetsDBOS.toTypedArray()
            )
        }

        _timerEditModeFlow.value = TimerEditMode.Idle

        notUpdatedTimerPresets.clear()
    }

    fun swap(fromIndex: Int, toIndex: Int) {
        val from = timerPresets.removeAt(fromIndex)
        timerPresets.add(toIndex, from)
    }
}
