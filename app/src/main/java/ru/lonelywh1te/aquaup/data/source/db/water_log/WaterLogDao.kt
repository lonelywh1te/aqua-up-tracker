package ru.lonelywh1te.aquaup.data.source.db.water_log

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDateTime

@Dao
interface WaterLogDao {

    @Insert
    fun addWaterLog(waterLog: WaterLogEntity)

    @Query("SELECT * FROM water_log WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getWaterLogsForPeriod(startDate: LocalDateTime, endDate: LocalDateTime): List<WaterLogEntity>

}