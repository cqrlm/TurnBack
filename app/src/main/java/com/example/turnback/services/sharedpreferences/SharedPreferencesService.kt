package com.example.turnback.services.sharedpreferences

import android.content.Context
import com.example.turnback.services.stopwatch.StopwatchState
import com.example.turnback.ui.theme.ThemeState
import com.example.turnback.utils.enumValueOrDefault
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SharedPreferencesService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun saveTime(milliseconds: Long) {
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .edit()
            .run {
                putLong(PREFERENCES_TIME_KEY, milliseconds)
                apply()
            }
    }

    fun saveStopwatchState(stopwatchState: StopwatchState) {
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .edit()
            .run {
                putInt(PREFERENCES_STOPWATCH_STATE_KEY, stopwatchState.ordinal)
                apply()
            }
    }

    fun getTime(): Duration =
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .getLong(PREFERENCES_TIME_KEY, 0)
            .run { toDuration(DurationUnit.MILLISECONDS) }

    fun getStopwatchState(): StopwatchState =
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .getInt(PREFERENCES_STOPWATCH_STATE_KEY, StopwatchState.STOP.ordinal)
            .run { enumValueOrDefault(this, StopwatchState.STOP) }

    fun getThemeState(): ThemeState =
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .getInt(PREFERENCES_THEME_KEY, ThemeState.SYSTEM.ordinal)
            .run { enumValueOrDefault(this, ThemeState.SYSTEM) }

    fun setThemeState(themeState: ThemeState) {
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .edit()
            .run {
                putInt(PREFERENCES_THEME_KEY, themeState.ordinal)
                apply()
            }
    }

    companion object {

        private const val PREFERENCES_FILENAME = "preferences"
        private const val PREFERENCES_TIME_KEY = "time"
        private const val PREFERENCES_STOPWATCH_STATE_KEY = "stopwatch_state"
        private const val PREFERENCES_THEME_KEY = "theme"
    }
}
