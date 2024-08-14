package com.example.turnback.mappers

import com.example.database.entities.TimerPresetDBO
import com.example.turnback.model.TimerPreset
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun TimerPresetDBO.toTimerPreset(): TimerPreset =
    TimerPreset(order, duration.toDuration(DurationUnit.MILLISECONDS), id)

fun TimerPreset.toTimerPresetDBO(): TimerPresetDBO =
    TimerPresetDBO(id, order, duration.inWholeMilliseconds)
