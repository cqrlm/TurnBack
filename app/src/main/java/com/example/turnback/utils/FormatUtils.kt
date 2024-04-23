package com.example.turnback.utils

import kotlin.time.Duration

fun Duration.formatTime(): String =
    toComponents { hours, minutes, seconds, _ ->
        TIME_PATTERN.format(hours, minutes, seconds)
    }

fun Duration.formatElapsedTime(): String =
    toComponents { hours, minutes, seconds, _ ->
        if (hours > 0)  {
            TIME_PATTERN.format(hours, minutes, seconds)
        } else ELAPSED_TIME_PATTERN.format(minutes, seconds)
    }

private const val TIME_PATTERN = "%02d:%02d:%02d"
private const val ELAPSED_TIME_PATTERN = "%02d:%02d"
