package com.example.turnback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.services.TimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class MainViewModel @Inject constructor(
    private val timeService: TimeService
) : ViewModel() {

    val timeFlow: StateFlow<Duration> = timeService.timeFlow

    init {
        viewModelScope.launch {
            timeService.startTimeCounting()
        }
    }

    fun saveTime(time: Duration) {
        timeService.saveTime(time)
    }

    fun resetTime() {
        timeService.resetTime()
    }
}
