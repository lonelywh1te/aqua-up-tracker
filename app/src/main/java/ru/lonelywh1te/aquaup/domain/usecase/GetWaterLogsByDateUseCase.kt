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

class GetWaterLogsByDateUseCase(
    private val waterLogRepository: WaterLogRepository,
    private val settingsPreferences: SettingsPreferences,
) {
    operator fun invoke(date: LocalDate): Flow<Result<List<WaterLog>>> {
        val start = date.atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)

        return combine(
            waterLogRepository.getWaterLogsForPeriod(start, end),
            settingsPreferences.volumeUnitFlow
        ) { waterLogsResult, volumeUnit ->
            waterLogsResult.map { waterLogs ->
                waterLogs
                    .convertUnit(VolumeUnit.Ml, volumeUnit)
                    .sortedByDescending { it.timestamp }
            }
        }
    }
}