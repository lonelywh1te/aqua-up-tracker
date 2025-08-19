package ru.lonelywh1te.aquaup.presentation.history

import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalDateTime

sealed class HistoryScreenState {

    data class Success(
        val waterLogs: List<WaterLog>,
        val volumeUnit: VolumeUnit
    ): HistoryScreenState() {
        companion object {
            val preview = HistoryScreenState.Success(
                waterLogs = listOf(
                    WaterLog(amountMl = 100, timestamp = LocalDateTime.now()),
                    WaterLog(amountMl = 150, timestamp = LocalDateTime.now()),
                    WaterLog(amountMl = 9999, timestamp = LocalDateTime.now()),
                ),
                volumeUnit = VolumeUnit.Ml
            )
        }
    }

    data object Loading: HistoryScreenState()

    data class Error(val message: String): HistoryScreenState()
}