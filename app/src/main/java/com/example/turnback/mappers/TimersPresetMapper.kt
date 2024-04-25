package com.example.turnback.mappers

import com.example.turnback.database.entities.TimerPresetDBO
import com.example.turnback.model.TimerPreset
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun TimerPresetDBO.toTimerPreset(): TimerPreset =
    TimerPreset(order, duration.toDuration(DurationUnit.MILLISECONDS))

fun TimerPreset.toTimerPresetDBO(): TimerPresetDBO =
    TimerPresetDBO(order, duration.inWholeMilliseconds)
