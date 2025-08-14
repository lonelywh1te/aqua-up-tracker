package ru.lonelywh1te.aquaup.domain.usecase

import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository

class UpdateWaterLogUseCase(
    private val waterLogRepository: WaterLogRepository
) {
    suspend operator fun invoke(waterLog: WaterLog) {
        waterLogRepository.updateWaterLog(waterLog)
    }
}