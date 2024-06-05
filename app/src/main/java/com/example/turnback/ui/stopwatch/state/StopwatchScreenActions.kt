package com.example.turnback.ui.stopwatch.state

import com.example.turnback.ui.base.ScreenActions

data class StopwatchScreenActions(
    val start: () -> Unit = {},
    val pause: () -> Unit = {},
    val stop: () -> Unit = {}
) : ScreenActions
