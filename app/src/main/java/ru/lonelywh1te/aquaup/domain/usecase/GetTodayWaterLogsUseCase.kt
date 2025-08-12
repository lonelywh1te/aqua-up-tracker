package ru.lonelywh1te.aquaup.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import java.time.LocalDate

class GetTodayWaterLogsUseCase(
    private val waterLogRepository: WaterLogRepository,
) {
    operator fun invoke(): Flow<List<WaterLog>> {
        val start = LocalDate.now().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)

        return waterLogRepository.getWaterLogsForPeriod(start, end).map { list ->
            list.sortedBy { it.timestamp }
        }
    }
}