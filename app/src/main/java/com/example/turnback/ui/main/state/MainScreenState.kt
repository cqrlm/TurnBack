package com.example.turnback.ui.main.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.theme.ThemeState

data class MainScreenState(
    val currentScreen: Screen = Screen.BottomBarItem.Timer,
    val themeState: ThemeState = ThemeState.SYSTEM,
    val selectedTimerPresetsCount: Int = 0,
    val isEditingMode: MutableState<Boolean> = mutableStateOf(false)
)
