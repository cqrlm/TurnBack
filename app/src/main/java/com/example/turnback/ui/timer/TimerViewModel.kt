package com.example.turnback.ui.timer

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.database.entities.TimerPresetDBO
import com.example.turnback.database.repositories.TimerPresetRepository
import com.example.turnback.mappers.toTimerPreset
import com.example.turnback.mappers.toTimerPresetDBO
import com.example.turnback.model.TimerPreset
import com.example.turnback.services.timer.TimerService
import com.example.turnback.ui.timer.state.TimerScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timerService: TimerService,
    private val timerPresetRepository: TimerPresetRepository
) : ViewModel() {

    val screenState = combine(
        timerService.timeFlow,
        timerService.timerState,
        timerPresetRepository.getAll().flowOn(Dispatchers.IO)
    ) { time, timerState, timerPresets ->
        TimerScreenState(
            timerState = timerState,
            timerDuration = time,
            timerPresets = timerPresets
                .map(TimerPresetDBO::toTimerPreset)
                .sortedBy(TimerPreset::order)
                .toMutableStateList()
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
        viewModelScope.launch(Dispatchers.IO) {
            timerPresetRepository.insert(timerPreset.toTimerPresetDBO())
        }
    }

    fun delete(timerPresets: List<TimerPreset>) {
        viewModelScope.launch(Dispatchers.IO) {
            val timerPresetsDBOS = timerPresets.map(TimerPreset::toTimerPresetDBO).toTypedArray()
            timerPresetRepository.delete(*timerPresetsDBOS)
        }
    }
}
