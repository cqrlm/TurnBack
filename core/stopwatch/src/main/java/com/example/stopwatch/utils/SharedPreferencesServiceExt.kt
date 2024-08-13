package com.example.stopwatch.utils

import com.example.sharedpreferences.SharedPreferencesConstants
import com.example.sharedpreferences.SharedPreferencesService
import com.example.stopwatch.StopwatchState

internal fun SharedPreferencesService.getState(): StopwatchState =
    getEnumValue(
        SharedPreferencesConstants.PREFERENCES_STOPWATCH_STATE_KEY,
        StopwatchState.STOP
    )

internal fun SharedPreferencesService.saveState(state: StopwatchState) {
    saveEnumValue(
        SharedPreferencesConstants.PREFERENCES_STOPWATCH_STATE_KEY,
        state
    )
}
