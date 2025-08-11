package ru.lonelywh1te.aquaup.domain

import java.time.LocalDateTime

data class WaterLog(
    val id: Long,
    val amountMl: Int,
    val timestamp: LocalDateTime,
)
