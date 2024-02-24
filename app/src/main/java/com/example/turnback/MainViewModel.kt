package com.example.turnback

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.services.SharedPreferencesService
import com.example.turnback.services.TimeService
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesService = SharedPreferencesService(application.applicationContext)

    private val initialTime: Duration =
        sharedPreferencesService.getTime().toDuration(DurationUnit.MILLISECONDS)

    private val timeService = TimeService(initialTime, viewModelScope)

    val timeFlow: StateFlow<Duration> = timeService.timeFlow

    init {
        timeService.startTimeCounting()
    }

    fun saveTime(time: Duration) {
        sharedPreferencesService.saveTime(time.inWholeMilliseconds)
    }

    fun resetTime() {
        timeService.resetTime()
    }
}
