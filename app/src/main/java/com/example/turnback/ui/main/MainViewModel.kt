package com.example.turnback.ui.main

import androidx.lifecycle.viewModelScope
import com.example.turnback.navigaiton.Screen
import com.example.turnback.services.SharedPreferencesService
import com.example.turnback.services.timer.preset.TimerEditMode
import com.example.turnback.services.timer.preset.TimerPresetManager
import com.example.turnback.ui.base.ScreenViewModel
import com.example.turnback.ui.main.state.MainScreenActions
import com.example.turnback.ui.main.state.MainScreenState
import com.example.turnback.ui.theme.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferencesService: SharedPreferencesService,
    private val timerPresetManager: TimerPresetManager
) : ScreenViewModel<MainScreenState, MainScreenActions>() {

    private val themeStateFlow = MutableStateFlow(sharedPreferencesService.getThemeState())
    private val currentScreenFlow = MutableStateFlow(Screen.START_DESTINATION)

    override val screenState: StateFlow<MainScreenState> =
        combine(
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

    override val screenActions: MainScreenActions =
        MainScreenActions(
            changeScreen = ::changeScreen,
            changeTheme = ::setThemeState,
            cancelDeletion = ::cancelDeletion,
            deleteTimerPresets = ::deleteTimerPresets,
            cancelEditing = ::cancelEditing,
            finishEditing = ::finishEditing
        )

    private fun cancelDeletion() {
        timerPresetManager.cancelDeletion()
    }

    private fun deleteTimerPresets() {
        viewModelScope.launch {
            timerPresetManager.deleteSelectedTimerPresets()
        }
    }

    private fun setThemeState(themeState: ThemeState) {
        themeStateFlow.value = themeState

        sharedPreferencesService.setThemeState(themeState)
    }

    private fun changeScreen(newScreen: Screen) {
        when (screenState.value.timerEditMode) {
            is TimerEditMode.Deletion -> timerPresetManager.cancelDeletion()
            is TimerEditMode.Editing -> timerPresetManager.cancelEditing()
            is TimerEditMode.Idle -> Unit
        }

        currentScreenFlow.value = newScreen
    }

    private fun cancelEditing() {
        timerPresetManager.cancelEditing()
    }

    private fun finishEditing() {
        viewModelScope.launch {
            timerPresetManager.saveEditingChanges()
        }
    }
}
