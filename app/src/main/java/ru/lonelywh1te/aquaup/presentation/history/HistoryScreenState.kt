package ru.lonelywh1te.aquaup.presentation.history

import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.WaterLog
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.presentation.ui.components.charts.barchart.BarChartEntry
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

sealed class HistoryScreenState {

    data class Success(
        val volumeUnit: VolumeUnit,
        val waterLogs: List<WaterLog>,
        val historyDate: LocalDate,
        val waterGoal: WaterGoal,
        val weekStart: LocalDate,
        val chartData: List<BarChartEntry>
    ): HistoryScreenState() {
        companion object {
            val preview = Success(
                waterLogs = listOf(
                    WaterLog(amountMl = 100, timestamp = LocalDateTime.now()),
                    WaterLog(amountMl = 150, timestamp = LocalDateTime.now()),
                    WaterLog(amountMl = 9999, timestamp = LocalDateTime.now()),
                ),
                volumeUnit = VolumeUnit.Ml,
                historyDate = LocalDate.now(),
                waterGoal = WaterGoal(1800),
                weekStart = LocalDate.now().with(DayOfWeek.MONDAY),
                chartData = listOf(
                    BarChartEntry("пн", 500f),
                    BarChartEntry("вт", 700f),
                    BarChartEntry("ср", 2000f),
                    BarChartEntry("чт", 500f),
                    BarChartEntry("пт", 500f),
                    BarChartEntry("сб", 500f),
                    BarChartEntry("вс", 500f),
                ),
            )
        }
    }

    data object Loading: HistoryScreenState()

    data class Error(val message: String): HistoryScreenState()
}