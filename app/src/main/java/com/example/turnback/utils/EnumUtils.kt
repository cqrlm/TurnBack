package com.example.turnback.utils

import kotlin.enums.enumEntries

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T : Enum<T>> enumValueOrDefault(index: Int, defaultValue: T): T =
    runCatching { enumEntries<T>()[index] }.getOrDefault(defaultValue)
