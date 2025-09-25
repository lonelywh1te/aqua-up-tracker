package ru.lonelywh1te.aquaup.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.result.Result
import java.time.LocalDate

class GetTodayWaterLogsUseCase(
    private val getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase,
) {
    operator fun invoke(): Flow<Result<List<WaterLog>>> {
        return getWaterLogsByDateUseCase(LocalDate.now())
    }
}