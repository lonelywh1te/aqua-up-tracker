package ru.lonelywh1te.aquaup.domain.usecase

import kotlinx.coroutines.flow.first
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.convertUnit
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.convertOzToMl
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences

class UpdateWaterLogUseCase(
    private val waterLogRepository: WaterLogRepository,
    private val settingsPreferences: SettingsPreferences,
) {
    suspend operator fun invoke(waterLog: WaterLog) {
        val volumeUnit = settingsPreferences.volumeUnitFlow.first()
        val waterLog = waterLog.convertUnit(volumeUnit, VolumeUnit.Ml)

        waterLogRepository.updateWaterLog(waterLog)
    }
}