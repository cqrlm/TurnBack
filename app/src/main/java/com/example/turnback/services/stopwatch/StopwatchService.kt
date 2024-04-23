package com.example.turnback.services.stopwatch

import com.example.turnback.services.SharedPreferencesService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class StopwatchService @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService
) {

    private val _timeFlow = MutableStateFlow(sharedPreferencesService.getTime())
    val timeFlow = _timeFlow.asStateFlow()

    private var stopwatchStateFlow = MutableStateFlow(sharedPreferencesService.getStopwatchState())
    val stopwatchState = stopwatchStateFlow.asStateFlow()

    private var timer: Timer? = null

    suspend fun observeStopwatchState() {
        var time = _timeFlow.value

        stopwatchStateFlow.collect { stopwatchState ->
            when (stopwatchState) {
                StopwatchState.START -> {
                    timer = fixedRateTimer(period = TIME_INTERVAL) {
                        time += TIME_INTERVAL_DURATION
                        _timeFlow.tryEmit(time)
                    }
                }

                StopwatchState.STOP -> {
                    timer?.cancel()
                    time = Duration.ZERO
                    _timeFlow.emit(time)
                }

                StopwatchState.PAUSE -> timer?.cancel()
            }
        }
    }

    fun saveTime(time: Duration) {
        sharedPreferencesService.saveTime(time.inWholeMilliseconds)
        sharedPreferencesService.saveStopwatchState(stopwatchStateFlow.value)
    }

    suspend fun start() {
        stopwatchStateFlow.emit(StopwatchState.START)
    }

    suspend fun pause() {
        stopwatchStateFlow.emit(StopwatchState.PAUSE)
    }

    suspend fun stop() {
        stopwatchStateFlow.emit(StopwatchState.STOP)
    }

    companion object {
        private const val TIME_INTERVAL = 1000L
        private val TIME_INTERVAL_DURATION = TIME_INTERVAL.milliseconds
    }
}
