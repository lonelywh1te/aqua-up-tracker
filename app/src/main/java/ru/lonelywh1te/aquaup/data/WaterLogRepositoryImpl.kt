package ru.lonelywh1te.aquaup.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogDao
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import java.time.LocalDateTime


class WaterLogRepositoryImpl(
    private val waterLogDao: WaterLogDao,
): WaterLogRepository {
    override fun getWaterLogsForPeriod(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<List<WaterLog>> {
        return waterLogDao.getWaterLogsForPeriod(start, end)
            .map { list -> list.map { it.toWaterLog() } }
    }

    override suspend fun addWaterLog(waterLog: WaterLog) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWaterLog(waterLog: WaterLog) {
        TODO("Not yet implemented")
    }

    override suspend fun updateWaterLog(waterLog: WaterLog) {
        TODO("Not yet implemented")
    }

}