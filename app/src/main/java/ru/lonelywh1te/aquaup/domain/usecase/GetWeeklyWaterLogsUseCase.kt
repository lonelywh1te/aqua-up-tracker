package ru.lonelywh1te.aquaup.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import java.time.LocalDate

class GetWeeklyWaterLogsUseCase(
    private val waterLogRepository: WaterLogRepository,
) {
    operator fun invoke(weekStart: LocalDate): Flow<Map<LocalDate, List<WaterLog>>> {
        val start = weekStart.atStartOfDay()
        val end = weekStart.plusDays(6).atStartOfDay()

        return waterLogRepository.getWaterLogsForPeriod(start, end)
            .map { waterLogs ->
                waterLogs.groupByDateWithEmptyDays(weekStart)
            }
    }

    private fun List<WaterLog>.groupByDateWithEmptyDays(weekStart: LocalDate): Map<LocalDate, List<WaterLog>> {
        val start = weekStart.atStartOfDay()
        val allDates = List(7) { it -> start.plusDays(it.toLong()).toLocalDate() }

        val grouped = this.groupBy { it.timestamp.toLocalDate() }

        return allDates.associateWith { date -> grouped[date] ?: emptyList() }
    }
}