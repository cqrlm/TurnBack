package com.example.turnback.services

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TimeService(val initialTime: Duration) {

    val timeFlow = flow {
        var time = initialTime

        while (true) {
            if (resetTime) {
                time = Duration.ZERO
                resetTime = false
            } else {
                delay(TIME_INTERVAL)
                time += TIME_INTERVAL
            }

            emit(time)
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
