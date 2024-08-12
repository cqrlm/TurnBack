package com.example.sharedpreferences

import android.content.Context
import com.example.common.enumValueOrDefault
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SharedPreferencesService @Inject constructor(
    @ApplicationContext val context: Context
) {

    inline fun <reified T : Enum<T>> getEnumValue(key: String, defaultValue: T): T =
        context
            .getSharedPreferences(
                SharedPreferencesConstants.PREFERENCES_FILENAME,
                Context.MODE_PRIVATE
            )
            .getInt(key, defaultValue.ordinal)
            .run { enumValueOrDefault(this, defaultValue) }

    fun <T : Enum<T>> saveEnumValue(key: String, value: T) {
        context
            .getSharedPreferences(
                SharedPreferencesConstants.PREFERENCES_FILENAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .run {
                putInt(key, value.ordinal)
                apply()
            }
    }

    fun getTime(): Duration =
        context
            .getSharedPreferences(
                SharedPreferencesConstants.PREFERENCES_FILENAME,
                Context.MODE_PRIVATE
            )
            .getLong(SharedPreferencesConstants.PREFERENCES_TIME_KEY, 0)
            .run { toDuration(DurationUnit.MILLISECONDS) }

    fun saveTime(milliseconds: Long) {
        context
            .getSharedPreferences(
                SharedPreferencesConstants.PREFERENCES_FILENAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .run {
                putLong(SharedPreferencesConstants.PREFERENCES_TIME_KEY, milliseconds)
                apply()
            }
    }
}
