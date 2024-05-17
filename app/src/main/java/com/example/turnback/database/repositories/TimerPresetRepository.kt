package com.example.turnback.database.repositories

import com.example.turnback.database.dao.TimerPresetDao
import com.example.turnback.database.entities.TimerPresetDBO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerPresetRepository @Inject constructor(
    private val timerPresetDao: TimerPresetDao
) {

    fun getAll(): Flow<List<TimerPresetDBO>> =
        timerPresetDao.getAllFlow()

    suspend fun insert(timerPresetDBO: TimerPresetDBO) {
        timerPresetDao.insert(timerPresetDBO)
    }

    suspend fun update(timerPresetDBO: TimerPresetDBO) {
        timerPresetDao.update(timerPresetDBO)
    }

    suspend fun delete(vararg timerPresetDBOS: TimerPresetDBO) {
        timerPresetDao.delete(*timerPresetDBOS)

        val reorderedTimerPresets = timerPresetDao
            .getAll()
            .mapIndexed { index, timerPreset -> timerPreset.copy(order = index) }
            .toTypedArray()

        timerPresetDao.update(*reorderedTimerPresets)
    }
}
