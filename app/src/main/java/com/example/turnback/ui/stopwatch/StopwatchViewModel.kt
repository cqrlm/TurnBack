package com.example.turnback.ui.stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.services.stopwatch.StopwatchService
import com.example.turnback.services.stopwatch.StopwatchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchService: StopwatchService
) : ViewModel() {

    val timeFlow: StateFlow<Duration> = stopwatchService.timeFlow
    val stopwatchState: StateFlow<StopwatchState> = stopwatchService.stopwatchState

    init {
        viewModelScope.launch {
            stopwatchService.observeStopwatchState()
        }
    }

    fun start() {
        viewModelScope.launch {
            stopwatchService.start()
        }
    }

    fun pause() {
        viewModelScope.launch {
            stopwatchService.pause()
        }
    }

    fun stop() {
        viewModelScope.launch {
            stopwatchService.stop()
        }
    }

    fun saveTime(time: Duration) {
        stopwatchService.saveTime(time)
    }
}
