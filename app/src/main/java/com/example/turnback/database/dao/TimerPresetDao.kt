package com.example.turnback.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.turnback.database.entities.TimerPreset

@Dao
interface TimerPresetDao {

    @Query("SELECT * FROM timer_presets")
    fun getAll(): List<TimerPreset>

    @Query("SELECT * FROM timer_presets WHERE duration = :duration")
    fun findByDuration(duration: Long) : TimerPreset

    @Insert
    fun insert(timerPreset: TimerPreset)

    @Update
    fun update(timerPreset: TimerPreset)

    @Update
    fun updateAll(vararg timerPresets: TimerPreset)

    @Delete
    fun delete(vararg timerPresets: TimerPreset)
}
