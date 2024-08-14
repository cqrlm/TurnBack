package com.example.turnback.di

import android.content.Context
import com.example.database.AppDatabase
import com.example.database.dao.TimerPresetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase =
        AppDatabase.getInstance(appContext)

    @Provides
    fun provideTimerPresetDao(appDatabase: AppDatabase): TimerPresetDao =
        appDatabase.timerPresetDao()
}
