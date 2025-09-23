package utils

import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

object WaterLogTestFactory {
    fun create(
        seed: Int,
        id: Long = Random.nextLong(),
        amountMl: Int = Random.nextInt(0, 1001),
        timestamp: LocalDateTime = LocalDateTime.of(
            LocalDate.of(Random.nextInt(1970, 2026), Random.nextInt(1, 13), Random.nextInt(1, 29)),
            LocalTime.of(Random.nextInt(24), Random.nextInt(60))
        )
    ): WaterLog {
        return WaterLog(id, amountMl, timestamp)
    }

    fun createList(seed: Int, count: Int): List<WaterLog> {
        return List(count) { create(seed) }
    }
}