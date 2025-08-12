package ru.lonelywh1te.aquaup.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.AddWaterUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetTodayWaterLogsUseCase

class HomeViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val getTodayWaterLogsUseCase: GetTodayWaterLogsUseCase,
    private val addWaterUseCase: AddWaterUseCase,
): ViewModel() {
    val state: StateFlow<HomeScreenState> = getTodayWaterLogsUseCase()
        .map { list ->
            HomeScreenState.Success(
                waterGoal = settingsPreferences.waterGoal,
                waterAmount = list.sumOf { it.amountMl },
                volumeUnit = settingsPreferences.volumeUnit,
                recentWaterVolumes = list.map { it.amountMl }.distinct().take(5)
            )
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeScreenState.Loading
        )

    fun onEvent(event: HomeScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeScreenEvent.AddWater -> addWaterUseCase(event.volume)
            }
        }
    }

}