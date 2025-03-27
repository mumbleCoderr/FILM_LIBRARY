package com.example.filmlibrary.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Long?.toLocalDate(): LocalDate? {
    return this?.let { millis ->
        Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

fun LocalDate.toMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}