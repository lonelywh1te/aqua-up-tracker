package ru.lonelywh1te.aquaup.data.source.db.water_log

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalDateTime

@Dao
interface WaterLogDao {

    @Query("SELECT * FROM water_log WHERE id = :id")
    suspend fun getWaterLog(id: Long): WaterLogEntity?

    @Insert
    suspend fun addWaterLog(waterLogEntity: WaterLogEntity)

    @Delete
    suspend fun deleteWaterLog(waterLogEntity: WaterLogEntity)

    @Update
    suspend fun updateWaterLog(waterLogEntity: WaterLogEntity)

    @Query("SELECT * FROM water_log WHERE timestamp BETWEEN :startDate AND :endDate")
    fun getWaterLogsForPeriod(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WaterLogEntity>>

}