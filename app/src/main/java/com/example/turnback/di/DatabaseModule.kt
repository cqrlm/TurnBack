package com.example.turnback.di

import android.content.Context
import androidx.room.Room
import com.example.turnback.database.AppDatabase
import com.example.turnback.database.dao.TimerPresetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room
            .databaseBuilder(appContext, AppDatabase::class.java, "app_database")
            .createFromAsset("database/timer_presets.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTimerPresetDao(appDatabase: AppDatabase): TimerPresetDao =
        appDatabase.timerPresetDao()
}
