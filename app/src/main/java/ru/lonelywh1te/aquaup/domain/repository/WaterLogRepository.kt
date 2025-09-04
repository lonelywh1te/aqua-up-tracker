package ru.lonelywh1te.aquaup.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.result.Result
import java.time.LocalDateTime

interface WaterLogRepository {

    fun getWaterLogsForPeriod(start: LocalDateTime, end: LocalDateTime): Flow<Result<List<WaterLog>>>

    suspend fun addWaterLog(waterLog: WaterLog): Result<Unit>

    suspend fun deleteWaterLog(waterLog: WaterLog): Result<Unit>

    suspend fun updateWaterLog(waterLog: WaterLog): Result<Unit>

}