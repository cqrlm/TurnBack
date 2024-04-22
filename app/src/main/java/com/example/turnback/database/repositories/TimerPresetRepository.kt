package com.example.turnback.database.repositories

import com.example.turnback.database.dao.TimerPresetDao
import com.example.turnback.database.entities.TimerPresetDBO
import javax.inject.Inject

class TimerPresetRepository @Inject constructor(
    private val timerPresetDao: TimerPresetDao
) {

    fun getAll(): List<TimerPresetDBO> =
        timerPresetDao.getAll()

    fun findByDuration(duration: Long): TimerPresetDBO =
        timerPresetDao.findByDuration(duration)

    fun insert(timerPresetDBO: TimerPresetDBO) {
        timerPresetDao.insert(timerPresetDBO)
    }

    fun update(timerPresetDBO: TimerPresetDBO) {
        timerPresetDao.update(timerPresetDBO)
    }

    fun updateAll(vararg timerPresetDBOS: TimerPresetDBO) {
        timerPresetDao.updateAll(*timerPresetDBOS)
    }

    fun delete(vararg timerPresetDBOS: TimerPresetDBO) {
        timerPresetDao.delete(*timerPresetDBOS)
    }
}
