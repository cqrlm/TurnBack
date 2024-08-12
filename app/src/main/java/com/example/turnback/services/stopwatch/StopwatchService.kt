package com.example.turnback.services.stopwatch

import com.example.turnback.services.sharedpreferences.SharedPreferencesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class StopwatchService @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService
) {

    private var timer: Timer? = null

    private val _timeFlow = MutableStateFlow(sharedPreferencesService.getTime())
    val timeFlow = _timeFlow.asStateFlow()

    private var _stopwatchStateFlow = MutableStateFlow(sharedPreferencesService.getStopwatchState())
    val stopwatchStateFlow = _stopwatchStateFlow.onEach { stopwatchState ->
        when (stopwatchState) {
            StopwatchState.START ->
                timer = fixedRateTimer(period = TIME_INTERVAL) {
                    _timeFlow.value += TIME_INTERVAL_DURATION
                }

            StopwatchState.PAUSE -> timer?.cancel()

            StopwatchState.STOP -> {
                timer?.cancel()
                _timeFlow.value = Duration.ZERO
            }
        }
    }

    fun saveTime(time: Duration) {
        sharedPreferencesService.saveTime(time.inWholeMilliseconds)
        sharedPreferencesService.saveStopwatchState(_stopwatchStateFlow.value)
    }

    fun start() {
        _stopwatchStateFlow.value = StopwatchState.START
    }

    fun pause() {
        _stopwatchStateFlow.value = StopwatchState.PAUSE
    }

    fun stop() {
        _stopwatchStateFlow.value = StopwatchState.STOP
    }

    companion object {
        private const val TIME_INTERVAL = 1000L
        private val TIME_INTERVAL_DURATION = TIME_INTERVAL.milliseconds
    }
}
