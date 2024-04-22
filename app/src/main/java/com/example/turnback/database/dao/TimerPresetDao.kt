package com.example.turnback.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.turnback.database.entities.TimerPresetDBO

@Dao
interface TimerPresetDao {

    @Query("SELECT * FROM timer_presets")
    fun getAll(): List<TimerPresetDBO>

    @Query("SELECT * FROM timer_presets WHERE duration = :duration")
    fun findByDuration(duration: Long) : TimerPresetDBO

    @Insert
    fun insert(timerPresetDBO: TimerPresetDBO)

    @Update
    fun update(timerPresetDBO: TimerPresetDBO)

    @Update
    fun updateAll(vararg timerPresetDBOS: TimerPresetDBO)

    @Delete
    fun delete(vararg timerPresetDBOS: TimerPresetDBO)
}
