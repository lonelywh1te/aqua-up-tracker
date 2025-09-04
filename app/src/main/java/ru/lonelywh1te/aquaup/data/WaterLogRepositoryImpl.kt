package ru.lonelywh1te.aquaup.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.lonelywh1te.aquaup.data.error.asDbSafeFlow
import ru.lonelywh1te.aquaup.data.error.dbRunCatchingWithResult
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogDao
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogEntity
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.result.Result
import java.time.LocalDateTime

class WaterLogRepositoryImpl(
    private val waterLogDao: WaterLogDao,
): WaterLogRepository {
    override fun getWaterLogsForPeriod(
        start: LocalDateTime,
        end: LocalDateTime
    ): Flow<Result<List<WaterLog>>> {
        return waterLogDao.getWaterLogsForPeriod(start, end)
            .map<List<WaterLogEntity>, Result<List<WaterLog>>> { list -> Result.Success(list.map { it.toWaterLog() }) }
            .asDbSafeFlow()
    }

    override suspend fun addWaterLog(waterLog: WaterLog): Result<Unit> {
        return dbRunCatchingWithResult {
            waterLogDao.addWaterLog(waterLog.toWaterLogEntity())
        }
    }

    override suspend fun deleteWaterLog(waterLog: WaterLog): Result<Unit> {
        return dbRunCatchingWithResult {
            waterLogDao.deleteWaterLog(waterLog.toWaterLogEntity())
        }
    }

    override suspend fun updateWaterLog(waterLog: WaterLog): Result<Unit> {
        return dbRunCatchingWithResult {
            waterLogDao.updateWaterLog(waterLog.toWaterLogEntity())
        }
    }
}