package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.database.dao.TimerPresetDao
import com.example.database.entities.TimerPresetDBO

@Database(entities = [TimerPresetDBO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun timerPresetDao(): TimerPresetDao

    companion object {

        fun getInstance(appContext: Context): AppDatabase =
            Room
                .databaseBuilder(appContext, AppDatabase::class.java, "app_database")
                .createFromAsset("database/timer_presets.db")
                .build()
    }
}
