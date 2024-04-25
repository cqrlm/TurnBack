package com.example.turnback.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.turnback.database.entities.TimerPresetDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerPresetDao {

    @Query("SELECT * FROM timer_presets")
    fun getAll(): Flow<List<TimerPresetDBO>>

    @Query("SELECT * FROM timer_presets WHERE duration = :duration")
    fun findByDuration(duration: Long) : Flow<TimerPresetDBO>

    @Insert
    suspend fun insert(timerPresetDBO: TimerPresetDBO)

    @Update
    suspend fun update(vararg timerPresetDBOS: TimerPresetDBO)

    @Delete
    suspend fun delete(vararg timerPresetDBOS: TimerPresetDBO)
}
