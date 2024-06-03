package com.example.turnback.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.navigaiton.Screen
import com.example.turnback.services.SharedPreferencesService
import com.example.turnback.services.TimerEditModeService
import com.example.turnback.services.timer.TimerEditMode
import com.example.turnback.services.timer.preset.TimerPresetService
import com.example.turnback.ui.main.state.MainScreenState
import com.example.turnback.ui.theme.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val timerPresetService: TimerPresetService,
    private val sharedPreferencesService: SharedPreferencesService,
    private val timerEditModeService: TimerEditModeService
) : ViewModel() {

    private val themeStateFlow = MutableStateFlow(sharedPreferencesService.getThemeState())
    private val currentScreenFlow = MutableStateFlow(Screen.START_DESTINATION)

    val screenState = combine(
        themeStateFlow,
        timerEditModeService.timerEditModeFlow,
        currentScreenFlow
    ) { themeState, timerEditMode, currentScreen ->
        MainScreenState(
            themeState = themeState,
            timerEditMode = timerEditMode,
            currentScreen = currentScreen
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MainScreenState())

    fun cancelDeletion() {
        timerPresetService.clearSelectedTimerPresets()
        timerEditModeService.setTimerEditMode(TimerEditMode.Idle)
    }

    fun deleteTimerPresets() {
        viewModelScope.launch {
            timerPresetService.deleteSelectedTimerPresets()
        }
        timerEditModeService.setTimerEditMode(TimerEditMode.Idle)
    }

    fun setThemeState(themeState: ThemeState) {
        themeStateFlow.value = themeState

        sharedPreferencesService.setThemeState(themeState)
    }

    fun changeScreen(newScreen: Screen) {
        if (screenState.value.timerEditMode is TimerEditMode.Deletion) {
            timerPresetService.clearSelectedTimerPresets()
        }

        currentScreenFlow.value = newScreen
        timerEditModeService.setTimerEditMode(TimerEditMode.Idle)
    }

    fun finishEditing() {
        timerEditModeService.setTimerEditMode(TimerEditMode.Idle)
    }
}
