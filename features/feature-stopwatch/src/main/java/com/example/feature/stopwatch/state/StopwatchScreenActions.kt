package com.example.feature.stopwatch.state

import com.example.architecture.ScreenActions

data class StopwatchScreenActions(
    val start: () -> Unit = {},
    val pause: () -> Unit = {},
    val stop: () -> Unit = {}
) : ScreenActions
