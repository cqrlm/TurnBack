package com.example.feature.stopwatch

import androidx.lifecycle.viewModelScope
import com.example.architecture.ScreenViewModel
import com.example.feature.stopwatch.state.StopwatchScreenActions
import com.example.feature.stopwatch.state.StopwatchScreenState
import com.example.stopwatch.StopwatchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val stopwatchService: StopwatchService
) : ScreenViewModel<StopwatchScreenState, StopwatchScreenActions>() {

    override val screenState: StateFlow<StopwatchScreenState> =
        combine(
            stopwatchService.timeFlow,
            stopwatchService.stopwatchStateFlow
        ) { time, stopwatchState ->
            StopwatchScreenState(
                time = time,
                stopwatchState = stopwatchState
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, StopwatchScreenState())

    override val screenActions: StopwatchScreenActions =
        StopwatchScreenActions(
            start = ::start,
            pause = ::pause,
            stop = ::stop
        )

    fun saveTime(time: Duration) {
        stopwatchService.saveTime(time)
    }

    private fun start() {
        stopwatchService.start()
    }

    private fun pause() {
        stopwatchService.pause()
    }

    private fun stop() {
        stopwatchService.stop()
    }
}
