package com.example.turnback.ui.timer

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerPresetService
import com.example.turnback.services.timer.TimerService
import com.example.turnback.ui.timer.state.TimerScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerService: TimerService,
    private val timerPresetService: TimerPresetService
) : ViewModel() {

    val screenState = combine(
        timerService.timeFlow,
        timerService.timerState,
        timerPresetService.timerPresetsFlow
    ) { time, timerState, timerPresets ->
        TimerScreenState(
            timerState = timerState,
            timerDuration = time,
            timerPresets = timerPresets.toMutableStateList()
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

    fun save(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetService.saveToDB(timerPreset)
        }
    }

    fun update(timerPreset: TimerPreset) {
        viewModelScope.launch {
            timerPresetService.updateInDB(timerPreset)
        }
    }

    fun select(timerPreset: TimerPreset) {
        timerPresetService.selectTimerPreset(timerPreset)
    }

    fun unselect(timerPreset: TimerPreset) {
        timerPresetService.unselectTimerPreset(timerPreset)
    }
}
