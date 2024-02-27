package com.example.turnback.services

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimeService @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService
) {

    private val initialTime: Duration =
        sharedPreferencesService.getTime().toDuration(DurationUnit.MILLISECONDS)

    private val _timeFlow = MutableStateFlow(initialTime)
    val timeFlow = _timeFlow.asStateFlow()

    private var resetTime = false

    suspend fun startTimeCounting() {
        var time = _timeFlow.value

        while (true) {
            if (resetTime) {
                time = Duration.ZERO
                resetTime = false
            } else {
                delay(TIME_INTERVAL)
                time += TIME_INTERVAL
            }

            _timeFlow.emit(time)
        }
    }

    fun saveTime(time: Duration) {
        sharedPreferencesService.saveTime(time.inWholeMilliseconds)
    }

    fun resetTime() {
        resetTime = true
    }

    companion object {
        private val TIME_INTERVAL = 1.seconds
    }
}
