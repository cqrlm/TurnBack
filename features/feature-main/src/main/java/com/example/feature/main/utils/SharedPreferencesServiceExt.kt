package com.example.feature.main.utils

import com.example.sharedpreferences.SharedPreferencesConstants
import com.example.sharedpreferences.SharedPreferencesService
import com.example.ui.theme.ThemeState

internal fun SharedPreferencesService.getState(): ThemeState =
    getEnumValue(
        SharedPreferencesConstants.PREFERENCES_THEME_KEY,
        ThemeState.SYSTEM
    )

internal fun SharedPreferencesService.saveState(state: ThemeState) {
    saveEnumValue(
        SharedPreferencesConstants.PREFERENCES_THEME_KEY,
        state
    )
}
