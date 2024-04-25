package com.example.turnback.database.repositories

import com.example.turnback.database.dao.TimerPresetDao
import com.example.turnback.database.entities.TimerPresetDBO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerPresetRepository @Inject constructor(
    private val timerPresetDao: TimerPresetDao
) {

    fun getAll(): Flow<List<TimerPresetDBO>> =
        timerPresetDao.getAll()

    fun findByDuration(duration: Long): Flow<TimerPresetDBO> =
        timerPresetDao.findByDuration(duration)

    suspend fun insert(timerPresetDBO: TimerPresetDBO) {
        timerPresetDao.insert(timerPresetDBO)
    }

    suspend fun update(vararg timerPresetDBOS: TimerPresetDBO) {
        timerPresetDao.update(*timerPresetDBOS)
    }

    suspend fun delete(vararg timerPresetDBOS: TimerPresetDBO) {
        timerPresetDao.delete(*timerPresetDBOS)
    }
}
