package com.example.turnback.ui.stopwatch.state

data class StopwatchScreenActions(
    val start: () -> Unit = {},
    val pause: () -> Unit = {},
    val stop: () -> Unit = {}
)
