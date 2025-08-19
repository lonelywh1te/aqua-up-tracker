package ru.lonelywh1te.aquaup.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.DeleteWaterLogUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetTodayWaterLogsUseCase
import ru.lonelywh1te.aquaup.domain.usecase.UpdateWaterLogUseCase

class HistoryViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val getTodayWaterLogsUseCase: GetTodayWaterLogsUseCase,
    private val updateWaterLogUseCase: UpdateWaterLogUseCase,
    private val deleteWaterLogUseCase: DeleteWaterLogUseCase,
): ViewModel() {
    val state: StateFlow<HistoryScreenState> = combine(
        getTodayWaterLogsUseCase(),
        settingsPreferences.volumeUnitFlow,
    ) { list, volumeUnit ->
        HistoryScreenState.Success(
            waterLogs = list,
            volumeUnit = settingsPreferences.volumeUnitFlow.first()
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryScreenState.Loading
    )

    fun onEvent(event: HistoryScreenEvent) {
        viewModelScope.launch {
            when(event) {
                is HistoryScreenEvent.WaterLogChanged -> {
                    updateWaterLogUseCase(event.waterLog)
                }
                is HistoryScreenEvent.WaterLogDeleted -> {
                    deleteWaterLogUseCase(event.waterLog)
                }
            }
        }
    }
}