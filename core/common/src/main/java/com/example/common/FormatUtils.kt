package com.example.common

import kotlin.time.Duration

fun Duration.formatTime(): String =
    toComponents { hours, minutes, seconds, _ ->
        TIME_PATTERN.format(hours, minutes, seconds)
    }

fun Duration.formatElapsedTime(): String =
    buildString {
        if (isNegative()) {
            append(MINUS_SIGN)
        }

        absoluteValue.toComponents { hours, minutes, seconds, _ ->
            if (hours > 0) {
                append(TIME_PATTERN.format(hours, minutes, seconds))
            } else append(ELAPSED_TIME_PATTERN.format(minutes, seconds))
        }
    }

private const val TIME_PATTERN = "%02d:%02d:%02d"
private const val ELAPSED_TIME_PATTERN = "%02d:%02d"
private const val MINUS_SIGN = '-'
