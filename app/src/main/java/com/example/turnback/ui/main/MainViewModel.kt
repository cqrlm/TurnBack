package com.example.turnback.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turnback.navigaiton.Screen
import com.example.turnback.services.SharedPreferencesService
import com.example.turnback.services.timer.preset.TimerEditMode
import com.example.turnback.services.timer.preset.TimerPresetManager
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
    private val sharedPreferencesService: SharedPreferencesService,
    private val timerPresetManager: TimerPresetManager
) : ViewModel() {

    private val themeStateFlow = MutableStateFlow(sharedPreferencesService.getThemeState())
    private val currentScreenFlow = MutableStateFlow(Screen.START_DESTINATION)

    val screenState = combine(
        themeStateFlow,
        timerPresetManager.timerEditModeFlow,
        currentScreenFlow
    ) { themeState, timerEditMode, currentScreen ->
        MainScreenState(
            themeState = themeState,
            timerEditMode = timerEditMode,
            currentScreen = currentScreen
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MainScreenState())

    fun cancelDeletion() {
        timerPresetManager.cancelDeletion()
    }

    fun deleteTimerPresets() {
        viewModelScope.launch {
            timerPresetManager.deleteSelectedTimerPresets()
        }
    }

    fun setThemeState(themeState: ThemeState) {
        themeStateFlow.value = themeState

        sharedPreferencesService.setThemeState(themeState)
    }

    fun changeScreen(newScreen: Screen) {
        when (screenState.value.timerEditMode) {
            is TimerEditMode.Deletion -> timerPresetManager.cancelDeletion()
            is TimerEditMode.Editing -> timerPresetManager.cancelEditing()
            is TimerEditMode.Idle -> Unit
        }

        currentScreenFlow.value = newScreen
    }

    fun cancelEditing() {
        timerPresetManager.cancelEditing()
    }

    fun finishEditing() {
        viewModelScope.launch {
            timerPresetManager.saveEditingChanges()
        }
    }
}
