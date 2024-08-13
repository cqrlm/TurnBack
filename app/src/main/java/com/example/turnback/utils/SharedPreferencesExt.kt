package com.example.turnback.utils

import com.example.sharedpreferences.SharedPreferencesConstants
import com.example.sharedpreferences.SharedPreferencesService
import com.example.stopwatch.StopwatchState
import com.example.ui.theme.ThemeState

inline fun <reified T : Enum<T>> SharedPreferencesService.getState(): T =
    when (T::class) {
        StopwatchState::class -> getEnumValue<T>(
            SharedPreferencesConstants.PREFERENCES_STOPWATCH_STATE_KEY,
            StopwatchState.STOP as T
        )

        ThemeState::class -> getEnumValue<T>(
            SharedPreferencesConstants.PREFERENCES_THEME_KEY,
            ThemeState.SYSTEM as T
        )

        else -> error("Unknown state type")
    }

inline fun <reified T : Enum<T>> SharedPreferencesService.saveState(state: T) {
    when (T::class) {
        StopwatchState::class -> saveEnumValue(
            SharedPreferencesConstants.PREFERENCES_STOPWATCH_STATE_KEY,
            state
        )

        ThemeState::class -> saveEnumValue(
            SharedPreferencesConstants.PREFERENCES_THEME_KEY,
            state
        )

        else -> error("Unknown state type")
    }
}
