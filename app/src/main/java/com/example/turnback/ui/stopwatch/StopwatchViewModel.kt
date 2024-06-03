package com.example.turnback.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.services.stopwatch.StopwatchService
import com.example.turnback.ui.stopwatch.state.StopwatchScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchService: StopwatchService
) : ViewModel() {

    val screenState = combine(
        stopwatchService.timeFlow,
        stopwatchService.stopwatchState
    ) { time, stopwatchState ->
        StopwatchScreenState(
            time = time,
            stopwatchState = stopwatchState
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, StopwatchScreenState())

    init {
        viewModelScope.launch {
            stopwatchService.observeStopwatchState()
        }
    }

    fun start() {
        stopwatchService.start()
    }

    fun pause() {
        stopwatchService.pause()
    }

    fun stop() {
        stopwatchService.stop()
    }

    fun saveTime(time: Duration) {
        stopwatchService.saveTime(time)
    }
}
