package ru.lonelywh1te.aquaup.presentation.ui.utils

import android.util.Log
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.toStringFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun LocalTime.getLocalTimesTo(end: LocalTime, stepHours: Long): List<LocalTime> {
    val times = mutableListOf<LocalTime>()
    val start = this

    var time = start
    while (time.isAfter(start.minusNanos(1)) && time.isBefore(end.plusNanos(1))) {
        times.add(time)
        time = time.plusHours(stepHours)
    }

    return times
}