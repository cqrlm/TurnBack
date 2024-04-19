package com.example.turnback.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer_presets")
data class TimerPreset(
    @PrimaryKey val order: Int,
    val duration: Long
)
