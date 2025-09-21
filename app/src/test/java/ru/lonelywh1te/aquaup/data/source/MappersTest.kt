package ru.lonelywh1te.aquaup.data.source

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity
import ru.lonelywh1te.aquaup.data.toLocalTimeList
import ru.lonelywh1te.aquaup.data.toPrefsString
import ru.lonelywh1te.aquaup.data.toWaterLog
import ru.lonelywh1te.aquaup.data.toWaterLogEntity
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalDateTime
import java.time.LocalTime

class MappersTest {
    private val time = LocalDateTime.now()

    private val testWaterLogEntity: WaterLogEntity = WaterLogEntity(
        id = 0L, amountMl = 200, timestamp = time
    )
    private val testWaterLog: WaterLog = WaterLog(
        id = 0L, amountMl = 200, timestamp = time
    )

    private val testListLocalTime: List<LocalTime> = listOf(
        LocalTime.of(23, 59),
        LocalTime.of(22, 30),
        LocalTime.of(0, 0),
    )

    private val testPrefsLocalTimeString: String = "23:59,22:30,00:00"

    @Test
    fun `WaterLogEntity map to WaterLog correctly`(){
        val expected = testWaterLog
        val actual = testWaterLogEntity.toWaterLog()

        assertEquals(expected, actual)
    }

    @Test
    fun `WaterLog map to WaterLogEntity correctly`(){
        val expected = testWaterLogEntity
        val actual = testWaterLog.toWaterLogEntity()

        assertEquals(expected, actual)
    }

    @Test
    fun `List of LocalTime map to prefs string correctly`() {
        val expected = testPrefsLocalTimeString
        val actual = testListLocalTime.toPrefsString()

        assertEquals(expected, actual)
    }

    @Test
    fun `Empty list of LocalTime map to prefs string correctly`() {
        val expected = ""
        val actual = emptyList<LocalTime>().toPrefsString()

        assertEquals(expected, actual)
    }

    @Test
    fun `Prefs string map to list of LocalTime correctly`() {
        val expected = testListLocalTime
        val actual = testPrefsLocalTimeString.toLocalTimeList()

        assertEquals(expected, actual)
    }

    @Test
    fun `Empty prefs string map to list of LocalTime correctly`() {
        val expected = emptyList<LocalTime>()
        val actual = "".toLocalTimeList()

        assertEquals(expected, actual)
    }

}