package com.example.turnback.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.AppState
import com.example.turnback.navigaiton.Screen
import com.example.turnback.services.AppStateService
import com.example.turnback.services.SharedPreferencesService
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
    private val appStateService: AppStateService
) : ViewModel() {

    private val themeStateFlow = MutableStateFlow(sharedPreferencesService.getThemeState())

    val screenState = combine(
        themeStateFlow,
        appStateService.appStateFlow
    ) { themeState, appState ->
        MainScreenState(
            themeState = themeState,
            appState = appState
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MainScreenState())

    fun cancelDeletion() {
        timerPresetService.clearSelectedTimerPresets()
        appStateService.setAppState(AppState.Idle())
    }

    fun deleteTimerPresets() {
        viewModelScope.launch {
            timerPresetService.deleteSelectedTimerPresets()
        }
        appStateService.setAppState(AppState.Idle())
    }

    fun setThemeState(themeState: ThemeState) {
        themeStateFlow.value = themeState

        sharedPreferencesService.setThemeState(themeState)
    }

    fun changeScreen(newScreen: Screen) {
        val currentAppState = screenState.value.appState

        if (currentAppState.screen != newScreen) {
            if (currentAppState is AppState.Deletion) {
                timerPresetService.clearSelectedTimerPresets()
            }

            appStateService.setAppState(AppState.Idle(newScreen))
        }
    }

    fun finishEditing() {
        appStateService.setAppState(AppState.Idle())
    }
}
