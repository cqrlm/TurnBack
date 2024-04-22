package com.example.turnback.ui.timer

import androidx.lifecycle.ViewModel
import com.example.turnback.services.timer.TimerService
import com.example.turnback.services.timer.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerService: TimerService
) : ViewModel() {

    val timeFlow: StateFlow<Duration> = timerService.timeFlow
    val timerState: StateFlow<TimerState> = timerService.timerState

    fun start(duration: Duration) {
        timerService.start(duration)
    }

    fun pause() {
        timerService.pause()
    }

    fun resume() {
        timerService.resume()
    }

    fun stop() {
        timerService.stop()
    }
}
