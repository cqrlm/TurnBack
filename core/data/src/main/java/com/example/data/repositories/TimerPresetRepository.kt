package com.example.data.repositories

import com.example.data.mappers.toTimerPreset
import com.example.data.mappers.toTimerPresetDBO
import com.example.data.model.TimerPreset
import com.example.database.dao.TimerPresetDao
import com.example.database.entities.TimerPresetDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerPresetRepository @Inject constructor(
    private val timerPresetDao: TimerPresetDao
) {

    fun getAll(): Flow<List<TimerPreset>> =
        timerPresetDao
            .getAllFlow()
            .map { it.map(TimerPresetDBO::toTimerPreset) }

    suspend fun insert(timerPreset: TimerPreset) {
        timerPresetDao.insert(timerPreset.toTimerPresetDBO())
    }

    suspend fun update(vararg timerPresets: TimerPreset) {
        val timerPresetsDBOS = timerPresets.map(TimerPreset::toTimerPresetDBO)

        timerPresetDao.update(*timerPresetsDBOS.toTypedArray())
    }

    suspend fun delete(vararg timerPresets: TimerPreset) {
        val timerPresetsDBOS = timerPresets.map(TimerPreset::toTimerPresetDBO)

        timerPresetDao.delete(*timerPresetsDBOS.toTypedArray())

        val reorderedTimerPresets = timerPresetDao
            .getAll()
            .mapIndexed { index, timerPreset -> timerPreset.copy(order = index) }
            .toTypedArray()

        timerPresetDao.update(*reorderedTimerPresets)
    }
}
