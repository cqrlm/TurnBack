package com.example.turnback.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.turnback.database.dao.TimerPresetDao
import com.example.turnback.database.entities.TimerPreset

@Database(entities = [TimerPreset::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun timerPresetDao() : TimerPresetDao
}