package ru.lonelywh1te.aquaup.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.GetWaterLogsByDateUseCase
import java.time.LocalDate
import kotlin.math.roundToInt

class MainViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val getWaterLogsByDateUseCase: GetWaterLogsByDateUseCase,
): ViewModel() {
    val state: StateFlow<MainScreenState> = combine(
        getWaterLogsByDateUseCase(LocalDate.now()),
        settingsPreferences.waterGoalFlow,
    ) { waterLogs, waterGoal ->
        val progressPercentage = if (waterGoal.value != 0) {
            ((waterLogs.sumOf { it.amountMl }.toFloat() / waterGoal.value) * 100).roundToInt()
        } else {
            0
        }

        MainScreenState(
            progressPercentage = progressPercentage
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainScreenState()
    )
}