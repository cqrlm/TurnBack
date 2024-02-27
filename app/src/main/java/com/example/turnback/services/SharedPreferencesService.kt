package com.example.turnback.services

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesService @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun saveTime(milliseconds: Long) =
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .edit()
            .run {
                putLong(PREFERENCES_TIME_KEY, milliseconds)
                apply()
            }

    fun getTime(): Long =
        context
            .getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
            .getLong(PREFERENCES_TIME_KEY, 0)

    companion object {

        private const val PREFERENCES_FILENAME = "preferences"
        private const val PREFERENCES_TIME_KEY = "time"
    }
}
