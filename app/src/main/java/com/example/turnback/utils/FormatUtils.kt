package com.example.turnback.utils

import kotlin.time.Duration

fun Duration.formatTime(): String =
    toComponents { hours, minutes, seconds, _ ->
        TIME_PATTERN.format(hours, minutes, seconds)
    }

private const val TIME_PATTERN = "%02d:%02d:%02d"
