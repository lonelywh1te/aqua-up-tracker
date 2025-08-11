package ru.lonelywh1te.aquaup.presentation.history

import ru.lonelywh1te.aquaup.presentation.history.domain.HistoryData

sealed class HistoryScreenEvent {
    data class HistoryChanged(val data: HistoryData): HistoryScreenEvent()
    data class HistoryDeleted(val data: HistoryData): HistoryScreenEvent()
}