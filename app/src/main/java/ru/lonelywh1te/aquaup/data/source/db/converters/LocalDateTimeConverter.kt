package ru.lonelywh1te.aquaup.data.source.db.converters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class LocalDateTimeConverter {

    @TypeConverter
    fun localDateTimeToLong(localDateTime: LocalDateTime): Long {
        return localDateTime
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
    @TypeConverter
    fun timeInMillisToLocalDateTime(millis: Long): LocalDateTime {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

}