package ru.lonelywh1te.aquaup.presentation.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.toStringFormat(): String {
    return when (this) {
        LocalDate.now() -> "Сегодня"
        LocalDate.now().minusDays(1) -> "Вчера"
        else -> this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }
}