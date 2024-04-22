package com.example.turnback.services.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.milliseconds

class TimerService @Inject constructor() {

    private val _timeFlow = MutableStateFlow(ZERO)
    val timeFlow = _timeFlow.asStateFlow()

    private var _timerStateFlow = MutableStateFlow(TimerState.STOP)
    val timerState = _timerStateFlow.asStateFlow()

    private var timer: Timer? = null

    fun start(duration: Duration) {
        stop()

        var time = duration

        _timerStateFlow.tryEmit(TimerState.START)

        timer = fixedRateTimer(period = TIME_INTERVAL) {
            _timeFlow.tryEmit(time)

            if (time != ZERO) {
                time -= TIME_INTERVAL_DURATION
            } else stop()
        }
    }

    fun pause() {
        timer?.cancel()
        _timerStateFlow.tryEmit(TimerState.PAUSE)
    }

    fun resume() {
        start(_timeFlow.value)
    }

    fun stop() {
        timer?.cancel()
        _timerStateFlow.tryEmit(TimerState.STOP)
    }

    companion object {
        private const val TIME_INTERVAL = 1000L
        private val TIME_INTERVAL_DURATION = TIME_INTERVAL.milliseconds
    }
}
