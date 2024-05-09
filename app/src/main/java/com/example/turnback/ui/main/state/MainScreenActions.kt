package com.example.turnback.ui.main.state

import com.example.turnback.navigaiton.Screen
import com.example.turnback.ui.theme.ThemeState

data class MainScreenActions(
    val changeScreen: (Screen) -> Unit,
    val changeTheme: (ThemeState) -> Unit,
    val clearSelection: () -> Unit,
    val deleteTimerPresets: () -> Unit
) {

    companion object {

        val Default = MainScreenActions(
            changeScreen = {},
            changeTheme = {},
            clearSelection = {},
            deleteTimerPresets = {}
        )
    }
}
