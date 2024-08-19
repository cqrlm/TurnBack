package com.example.turnback.ui.main.state

import com.example.architecture.ScreenState
import com.example.navigation.Screen
import com.example.timerpreset.TimerEditMode
import com.example.ui.theme.ThemeState

data class MainScreenState(
    val themeState: ThemeState = ThemeState.SYSTEM,
    val timerEditMode: TimerEditMode = TimerEditMode.Idle,
    val currentScreen: Screen = Screen.START_DESTINATION
) : ScreenState
