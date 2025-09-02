package ru.lonelywh1te.aquaup.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.convertToUnit
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.convertMlToOz
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import java.time.LocalDate

class GetTodayWaterLogsUseCase(
    private val waterLogRepository: WaterLogRepository,
    private val settingsPreferences: SettingsPreferences,
) {
    operator fun invoke(): Flow<List<WaterLog>> {
        val start = LocalDate.now().atStartOfDay()
        val end = start.plusDays(1).minusNanos(1)

        return combine(
            waterLogRepository.getWaterLogsForPeriod(start, end),
            settingsPreferences.volumeUnitFlow
        ) { waterLogs, volumeUnit ->
            waterLogs
                .convertToUnit(volumeUnit)
                .sortedByDescending { it.timestamp }
        }
    }
}