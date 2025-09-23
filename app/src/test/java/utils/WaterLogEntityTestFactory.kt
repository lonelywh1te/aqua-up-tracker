package utils

import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

object WaterLogEntityTestFactory {
    fun create(
        seed: Int,
        id: Long = Random(seed).nextLong(),
        amountMl: Int = Random(seed).nextInt(0, 1001),
        timestamp: LocalDateTime = LocalDateTime.of(
            LocalDate.of(Random(seed).nextInt(1970, 2026), Random(seed).nextInt(1, 13), Random(seed).nextInt(1, 29)),
            LocalTime.of(Random(seed).nextInt(24), Random(seed).nextInt(60))
        )
    ): WaterLogEntity {
        return WaterLogEntity(id, amountMl, timestamp)
    }

    fun createList(seed: Int, count: Int): List<WaterLogEntity> {
        return List(count) { create(seed) }
    }
}