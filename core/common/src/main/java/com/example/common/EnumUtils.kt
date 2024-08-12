package com.example.common

import kotlin.enums.enumEntries

inline fun <reified T : Enum<T>> enumValueOrDefault(index: Int, defaultValue: T): T =
    runCatching { enumEntries<T>()[index] }.getOrDefault(defaultValue)
