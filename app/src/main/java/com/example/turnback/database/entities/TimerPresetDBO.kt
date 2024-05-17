package com.example.turnback.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @property order Order in the timer preset list.
 * @property duration Duration in milliseconds.
 */
@Entity(tableName = "timer_presets")
data class TimerPresetDBO(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val order: Int,
    val duration: Long
)
