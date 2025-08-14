package ru.lonelywh1te.aquaup.presentation.ui.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun LocalTime.toStringFormat(): String {
    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}