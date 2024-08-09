package com.example.turnback.services.timer

import android.app.Activity
import android.content.Context
import kotlin.time.Duration

data class TimerServiceActions(
    val start: (
        duration: Duration,
        context: Context,
        activityClassName: Class<out Activity>
    ) -> Unit,
    val pause: () -> Unit,
    val resume: () -> Unit,
    val stop: () -> Unit
)
