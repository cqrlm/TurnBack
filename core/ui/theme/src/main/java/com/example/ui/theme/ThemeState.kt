package com.example.ui.theme

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

enum class ThemeState(@StringRes val stringId: Int) {

    SYSTEM(R.string.system),
    LIGHT(R.string.light),
    DARK(R.string.dark);

    @Composable
    @ReadOnlyComposable
    fun isDarkTheme(): Boolean =
        when (this) {
            SYSTEM -> isSystemInDarkTheme()
            LIGHT -> false
            DARK -> true
        }
}
