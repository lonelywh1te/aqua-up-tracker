package ru.lonelywh1te.aquaup.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.DeleteWaterLogUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetWaterLogsByDateUseCase
import ru.lonelywh1te.aquaup.domain.usecase.UpdateWaterLogUseCase
import java.time.LocalDate

class HistoryViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase,
    private val updateWaterLogUseCase: UpdateWaterLogUseCase,
    private val deleteWaterLogUseCase: DeleteWaterLogUseCase,
): ViewModel() {
    private val _historyData: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<HistoryScreenState> = _historyData
        .flatMapLatest { historyData ->
            combine(
                getWaterLogsByDateUseCase(historyData),
                settingsPreferences.volumeUnitFlow,
            ) { waterLogs, volumeUnit ->
                HistoryScreenState.Success(
                    waterLogs = waterLogs,
                    volumeUnit = volumeUnit,
                    historyDate = historyData
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryScreenState.Loading)

    fun onEvent(event: HistoryScreenEvent) {
        viewModelScope.launch {
            when(event) {
                is HistoryScreenEvent.WaterLogChanged -> {
                    updateWaterLogUseCase(event.waterLog)
                }
                is HistoryScreenEvent.WaterLogDeleted -> {
                    deleteWaterLogUseCase(event.waterLog)
                }

                is HistoryScreenEvent.HistoryDateChanged -> _historyData.value = event.date
            }
        }
    }
}