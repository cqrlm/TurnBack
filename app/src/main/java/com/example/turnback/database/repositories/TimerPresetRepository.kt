package com.example.turnback.database.repositories

import com.example.turnback.database.dao.TimerPresetDao
import com.example.turnback.database.entities.TimerPreset
import javax.inject.Inject

class TimerPresetRepository @Inject constructor(
    private val timerPresetDao: TimerPresetDao
) {

    fun getAll(): List<TimerPreset> =
        timerPresetDao.getAll()

    fun findByDuration(duration: Long): TimerPreset =
        timerPresetDao.findByDuration(duration)

    fun insert(timerPreset: TimerPreset) {
        timerPresetDao.insert(timerPreset)
    }

    fun update(timerPreset: TimerPreset) {
        timerPresetDao.update(timerPreset)
    }

    fun updateAll(vararg timerPresets: TimerPreset) {
        timerPresetDao.updateAll(*timerPresets)
    }

    fun delete(vararg timerPresets: TimerPreset) {
        timerPresetDao.delete(*timerPresets)
    }
}
