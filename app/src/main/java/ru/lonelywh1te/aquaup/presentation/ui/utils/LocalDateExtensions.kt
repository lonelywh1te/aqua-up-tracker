package ru.lonelywh1te.aquaup.presentation.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toRelativeDateString(): String {
    return when (this) {
        LocalDate.now() -> "Сегодня"
        LocalDate.now().minusDays(1) -> "Вчера"
        else -> this.toDateString()
    }
}

fun LocalDate.toDateString(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}