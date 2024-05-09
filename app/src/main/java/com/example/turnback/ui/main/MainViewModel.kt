package com.example.turnback.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.services.SharedPreferencesService
import com.example.turnback.services.timer.TimerPresetSelectorService
import com.example.turnback.services.timer.TimerPresetService
import com.example.turnback.ui.theme.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val timerPresetSelectorService: TimerPresetSelectorService,
    private val timerPresetService: TimerPresetService,
    private val sharedPreferencesService: SharedPreferencesService
) : ViewModel() {

    val selectedTimerPresetsCount = timerPresetSelectorService.selectedTimerPresetsCountFlow

    val themeState = sharedPreferencesService.getThemeState()

    fun clearSelection() {
        viewModelScope.launch {
            timerPresetSelectorService.clear()
        }
    }

    fun deleteTimerPresets() {
        viewModelScope.launch {
            timerPresetService.deleteSelectedTimerPresets()
        }
    }

    fun setThemeState(themeState: ThemeState) {
        sharedPreferencesService.setThemeState(themeState)
    }
}
