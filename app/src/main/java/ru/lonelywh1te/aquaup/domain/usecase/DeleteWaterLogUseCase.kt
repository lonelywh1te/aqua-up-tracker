package ru.lonelywh1te.aquaup.domain.usecase

import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository

class DeleteWaterLogUseCase(
    private val waterLogRepository: WaterLogRepository,
) {
    suspend operator fun invoke(waterLog: WaterLog) {
        waterLogRepository.deleteWaterLog(waterLog)
    }
}