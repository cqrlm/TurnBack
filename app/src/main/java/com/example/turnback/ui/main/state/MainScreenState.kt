package com.example.turnback.ui.main.state

import com.example.turnback.navigaiton.Screen
import com.example.turnback.services.timer.preset.TimerEditMode
import com.example.turnback.ui.base.ScreenState
import com.example.turnback.ui.theme.ThemeState

data class MainScreenState(
    val themeState: ThemeState = ThemeState.SYSTEM,
    val timerEditMode: TimerEditMode = TimerEditMode.Idle,
    val currentScreen: Screen = Screen.START_DESTINATION
) : ScreenState
