package ru.lonelywh1te.aquaup.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.convertUnit
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.result.Result
import ru.lonelywh1te.aquaup.domain.result.map
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import java.time.LocalDate

class GetWeeklyWaterLogsUseCase(
    private val waterLogRepository: WaterLogRepository,
    private val settingsPreferences: SettingsPreferences,
) {
    operator fun invoke(weekStart: LocalDate): Flow<Result<Map<LocalDate, List<WaterLog>>>> {
        val start = weekStart.atStartOfDay()
        val end = weekStart.plusDays(6).atStartOfDay()

        return combine(
            waterLogRepository.getWaterLogsForPeriod(start, end),
            settingsPreferences.volumeUnitFlow
        ) { waterLogsResult, volumeUnit ->
            waterLogsResult.map { waterLogs ->
                waterLogs
                    .convertUnit(VolumeUnit.Ml, volumeUnit)
                    .groupByDateWithEmptyDays(weekStart)
            }
        }
    }

    private fun List<WaterLog>.groupByDateWithEmptyDays(weekStart: LocalDate): Map<LocalDate, List<WaterLog>> {
        val start = weekStart.atStartOfDay()
        val allDates = List(7) { it -> start.plusDays(it.toLong()).toLocalDate() }

        val grouped = this.groupBy { it.timestamp.toLocalDate() }

        return allDates.associateWith { date -> grouped[date] ?: emptyList() }
    }
}