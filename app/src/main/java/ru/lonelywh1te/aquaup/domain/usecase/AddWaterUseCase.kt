package ru.lonelywh1te.aquaup.domain.usecase

import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import java.time.LocalDateTime

class AddWaterUseCase(
    private val waterLogRepository: WaterLogRepository,
) {
    suspend operator fun invoke(amount: Int) {
        if (amount == 0) return

        val waterLog = WaterLog(
            amountMl = amount,
            timestamp = LocalDateTime.now()
        )

        waterLogRepository.addWaterLog(waterLog)
    }
}