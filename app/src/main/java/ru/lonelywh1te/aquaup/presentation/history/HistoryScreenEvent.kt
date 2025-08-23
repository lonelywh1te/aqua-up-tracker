package ru.lonelywh1te.aquaup.presentation.history

import ru.lonelywh1te.aquaup.domain.model.WaterLog
import java.time.LocalDate

sealed class HistoryScreenEvent {
    data class WaterLogChanged(val waterLog: WaterLog): HistoryScreenEvent()
    data class WaterLogDeleted(val waterLog: WaterLog): HistoryScreenEvent()
    data class HistoryDateChanged(val date: LocalDate): HistoryScreenEvent()
}