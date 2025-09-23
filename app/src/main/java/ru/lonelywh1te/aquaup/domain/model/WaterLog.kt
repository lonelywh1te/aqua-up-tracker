package ru.lonelywh1te.aquaup.domain.model

import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.convertMlToOz
import ru.lonelywh1te.aquaup.domain.model.settings.convertOzToMl
import java.time.LocalDateTime

data class WaterLog(
    val id: Long = 0L,
    val amountMl: Int = 0,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)

fun WaterLog.convertUnit(from: VolumeUnit, to: VolumeUnit): WaterLog {
    return this.copy(
        amountMl = when {
            from == VolumeUnit.Ml && to == VolumeUnit.Oz -> convertMlToOz(amountMl)
            from == VolumeUnit.Oz && to == VolumeUnit.Ml -> convertOzToMl(amountMl)
            else -> this.amountMl
        }
    )
}

fun List<WaterLog>.convertUnit(from: VolumeUnit, to: VolumeUnit): List<WaterLog> {
    return this.map { it.convertUnit(from, to) }
}