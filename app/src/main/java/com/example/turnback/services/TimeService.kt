package com.example.turnback.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimeService(initialTime: Duration, private val coroutineScope: CoroutineScope = MainScope()) {

    private val _timeFlow = MutableStateFlow(initialTime)
    val timeFlow = _timeFlow.asStateFlow()

    fun startTimeCounting() {
        var time = _timeFlow.value

        coroutineScope.launch {
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
    }

    private var resetTime = false

    fun resetTime() {
        resetTime = true
    }

    companion object {
        private val TIME_INTERVAL = 1.seconds
    }
}
