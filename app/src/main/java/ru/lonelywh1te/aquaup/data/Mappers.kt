package ru.lonelywh1te.aquaup.data

import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalTime

fun WaterLogEntity.toWaterLog(): WaterLog {
    return WaterLog(
        id = this.id,
        amountMl = this.amountMl,
        timestamp = this.timestamp,
    )
}

fun WaterLog.toWaterLogEntity(): WaterLogEntity {
    return WaterLogEntity(
        id = this.id,
        amountMl = this.amountMl,
        timestamp = this.timestamp,
    )
}

fun List<LocalTime>.toPrefsString(): String =
    joinToString(",") { it.toString() }

fun String.toLocalTimeList(): List<LocalTime> =
    split(",").filter { it.isNotBlank() }.map { LocalTime.parse(it) }