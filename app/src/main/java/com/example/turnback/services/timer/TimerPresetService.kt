package com.example.turnback.services.timer

import com.example.turnback.database.entities.TimerPresetDBO
import com.example.turnback.database.repositories.TimerPresetRepository
import com.example.turnback.mappers.toTimerPreset
import com.example.turnback.mappers.toTimerPresetDBO
import com.example.turnback.model.TimerPreset
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
@OptIn(ExperimentalCoroutinesApi::class)
class TimerPresetService @Inject constructor(
    private val timerPresetRepository: TimerPresetRepository,
    private val timerPresetSelectorService: TimerPresetSelectorService
) {

    private val databaseTimerPresetsFlow = timerPresetRepository
        .getAll()
        .flowOn(Dispatchers.IO)
        .flatMapConcat { timerPresetsDBOs ->
            flowOf(
                timerPresetsDBOs
                    .map(TimerPresetDBO::toTimerPreset)
                    .sortedBy(TimerPreset::order)
            )
        }

    val timerPresetsFlow = combine(
        databaseTimerPresetsFlow,
        timerPresetSelectorService.selectedTimerPresetsFlow
    ) { timerPresets, selectedTimerPresets ->
        val selectedTimerPresetsOrders = selectedTimerPresets.map(TimerPreset::order)

        timerPresets.map { timerPreset ->
            timerPreset.copy(selected = timerPreset.order in selectedTimerPresetsOrders)
        }
    }

    suspend fun saveToDB(timerPreset: TimerPreset) {
        withContext(Dispatchers.IO) {
            timerPresetRepository.insert(timerPreset.toTimerPresetDBO())
        }
    }

    fun selectTimerPreset(timerPreset: TimerPreset) {
        timerPresetSelectorService.select(timerPreset)
    }

    fun unselectTimerPreset(timerPreset: TimerPreset) {
        timerPresetSelectorService.unselect(timerPreset)
    }

    suspend fun deleteSelectedTimerPresets() {
        val timerPresetsDBOS = timerPresetSelectorService.convertSelectedTimerPresetsToDBOS()

        timerPresetSelectorService.clear()

        withContext(Dispatchers.IO) {
            timerPresetRepository.delete(*timerPresetsDBOS.toTypedArray())
        }
    }

}
