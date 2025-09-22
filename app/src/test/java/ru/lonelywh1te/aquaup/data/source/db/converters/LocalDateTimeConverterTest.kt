package ru.lonelywh1te.aquaup.data.source.db.converters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId

class LocalDateTimeConverterTest {
    private lateinit var converter: LocalDateTimeConverter
    private lateinit var time: LocalDateTime

    @BeforeEach
    fun setUp(){
        converter = LocalDateTimeConverter()
        time = LocalDateTime.now()
    }

    @Nested
    inner class localDateTimeToLongTest() {

        @Test
        fun `return correct converted value`() {
            val expected = time
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            val actual = converter.localDateTimeToLong(time)

            assertEquals(expected, actual)
        }

    }

    @Nested
    inner class timeInMillisToLocalDateTime() {
        @Test
        fun `return correct converted value`() {
            val expected = time.withNano((time.nano / 1_000_000) * 1_000_000)

            val actual = converter.timeInMillisToLocalDateTime(
                time.atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )

            assertEquals(expected, actual)
        }
    }

}