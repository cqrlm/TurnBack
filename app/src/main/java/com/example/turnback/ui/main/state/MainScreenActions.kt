package com.example.turnback.ui.main.state

import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.theme.ThemeState

data class MainScreenActions(
    val changeScreen: (Screen) -> Unit = {},
    val changeTheme: (ThemeState) -> Unit = {},
    val cancelDeletion: () -> Unit = {},
    val deleteTimerPresets: () -> Unit = {},
    val cancelEditing: () -> Unit = {},
    val finishEditing: () -> Unit = {}
)
