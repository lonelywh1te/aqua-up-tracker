package ru.lonelywh1te.aquaup.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalDateTime

interface WaterLogRepository {

    fun getWaterLogsForPeriod(start: LocalDateTime, end: LocalDateTime): Flow<List<WaterLog>>

    suspend fun addWaterLog(waterLog: WaterLog)

    suspend fun deleteWaterLog(waterLog: WaterLog)

    suspend fun updateWaterLog(waterLog: WaterLog)

}