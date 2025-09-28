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
import ru.lonelywh1te.aquaup.domain.result.combineResult
import ru.lonelywh1te.aquaup.domain.result.handle
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.DeleteWaterLogUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetWaterLogsByDateUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetWeeklyWaterLogsUseCase
import ru.lonelywh1te.aquaup.domain.usecase.UpdateWaterLogUseCase
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChartEntry
import ru.lonelywh1te.aquaup.presentation.ui.utils.asStringRes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class HistoryViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase,
    private val getWeeklyWaterLogsUseCase: GetWeeklyWaterLogsUseCase,
    private val updateWaterLogUseCase: UpdateWaterLogUseCase,
    private val deleteWaterLogUseCase: DeleteWaterLogUseCase,
    private val locale: Locale,
): ViewModel() {
    private val historyDate: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    private val weekStart: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now().with(DayOfWeek.MONDAY))

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<HistoryScreenState> = combine(
        historyDate.flatMapLatest { getWaterLogsByDateUseCase(it) },
        weekStart.flatMapLatest { getWeeklyWaterLogsUseCase(it) },
        settingsPreferences.volumeUnitFlow,
        settingsPreferences.waterGoalFlow,
    ) { waterLogsResult, weeklyWaterLogsResult, volumeUnit, waterGoal ->

        combineResult(waterLogsResult, weeklyWaterLogsResult) { waterLogs, weeklyWaterLogs ->
            HistoryScreenState.Success(
                waterLogs = waterLogs,
                volumeUnit = volumeUnit,
                historyDate = historyDate.value,
                waterGoal = waterGoal,
                weekStart = weekStart.value,
                chartData = weeklyWaterLogs.map { (date, logs) ->
                    BarChartEntry(
                        x = date.atStartOfDay().dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, locale),
                        y = logs.sumOf { it.amountMl }.toFloat()
                    )
                }
            )
        }.handle(
            onSuccess = { it },
            onLoading = { HistoryScreenState.Loading },
            onFailure = { HistoryScreenState.Error(it.asStringRes(), it.e.toString()) }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryScreenState.Loading)

    fun onEvent(event: HistoryScreenEvent) {
        viewModelScope.launch {
            when(event) {
                is HistoryScreenEvent.WaterLogChanged -> {
                    updateWaterLogUseCase(event.waterLog)
                }
                is HistoryScreenEvent.WaterLogDeleted -> {
                    deleteWaterLogUseCase(event.waterLog)
                }

                is HistoryScreenEvent.HistoryDateChanged -> {
                    historyDate.value = event.date
                }

                is HistoryScreenEvent.SetChartNextWeek -> {
                    weekStart.value = weekStart.value.plusWeeks(1)
                }

                is HistoryScreenEvent.SetChartPreviousWeek -> {
                    weekStart.value = weekStart.value.minusWeeks(1)
                }
            }
        }
    }
}