package com.example.turnback.model

import com.example.turnback.database.entities.TimerPresetDBO
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class TimerPreset(
    val order: Int,
    val duration: Duration
) {

    constructor(timerPresetDBO: TimerPresetDBO) : this(
        timerPresetDBO.order,
        timerPresetDBO.duration.toDuration(DurationUnit.MILLISECONDS)
    )
}
