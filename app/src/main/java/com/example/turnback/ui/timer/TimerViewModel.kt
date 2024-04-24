package com.example.turnback.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.services.timer.TimerService
import com.example.turnback.ui.timer.state.TimerScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerService: TimerService
) : ViewModel() {

    val screenState = combine(
        timerService.timeFlow,
        timerService.timerState
    ) { time, timerState ->
        TimerScreenState(
            timerState = timerState,
            timerDuration = time
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TimerScreenState())

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
