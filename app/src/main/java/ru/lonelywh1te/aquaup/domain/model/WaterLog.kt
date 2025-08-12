package ru.lonelywh1te.aquaup.domain.model

import java.time.LocalDateTime

data class WaterLog(
    val id: Long = 0L,
    val amountMl: Int,
    val timestamp: LocalDateTime,
)