package ru.lonelywh1te.aquaup.presentation.history

import ru.lonelywh1te.aquaup.domain.VolumeUnit
import ru.lonelywh1te.aquaup.presentation.history.domain.HistoryData
import java.time.LocalTime

data class HistoryScreenState(
    val historyData: List<HistoryData>,
    val volumeUnit: VolumeUnit,
) {
    companion object {
        fun getPreviewState(): HistoryScreenState {
            return HistoryScreenState(
                historyData = listOf(
                    HistoryData(200, LocalTime.of(22, 0)),
                    HistoryData(300, LocalTime.of(22, 0)),
                    HistoryData(1100, LocalTime.of(22, 0)),
                    HistoryData(100, LocalTime.of(22, 0)),
                    HistoryData(200, LocalTime.of(22, 0)),
                    HistoryData(300, LocalTime.of(22, 0)),
                ),
                volumeUnit = VolumeUnit.ML,
            )
        }
    }
}
