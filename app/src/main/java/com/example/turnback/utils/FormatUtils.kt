package com.example.turnback.utils

import android.text.format.DateUtils
import kotlin.time.Duration

fun Duration.formatTime(): String =
    toComponents { hours, minutes, seconds, _ ->
        TIME_PATTERN.format(hours, minutes, seconds)
    }

fun Duration.formatElapsedTime(): String =
    DateUtils.formatElapsedTime(inWholeSeconds)

private const val TIME_PATTERN = "%02d:%02d:%02d"
