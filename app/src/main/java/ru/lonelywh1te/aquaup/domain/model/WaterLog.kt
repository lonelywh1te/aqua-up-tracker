package ru.lonelywh1te.aquaup.domain.model

import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.convertMlToOz
import java.time.LocalDateTime

data class WaterLog(
    val id: Long = 0L,
    val amountMl: Int,
    val timestamp: LocalDateTime,
)

fun WaterLog.convertToUnit(volumeUnit: VolumeUnit): WaterLog {
    return when (volumeUnit) {
        VolumeUnit.Ml -> this.copy(amountMl = this.amountMl)
        VolumeUnit.Oz -> this.copy(amountMl = convertMlToOz(this.amountMl))
    }
}

fun List<WaterLog>.convertToUnit(volumeUnit: VolumeUnit): List<WaterLog> {
    return this.map { it.convertToUnit(volumeUnit) }
}