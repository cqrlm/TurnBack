package com.example.data.mappers

import com.example.data.model.TimerPreset
import com.example.database.entities.TimerPresetDBO
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun TimerPresetDBO.toTimerPreset(): TimerPreset =
    TimerPreset(order, duration.toDuration(DurationUnit.MILLISECONDS), id)

fun TimerPreset.toTimerPresetDBO(): TimerPresetDBO =
    TimerPresetDBO(id, order, duration.inWholeMilliseconds)
