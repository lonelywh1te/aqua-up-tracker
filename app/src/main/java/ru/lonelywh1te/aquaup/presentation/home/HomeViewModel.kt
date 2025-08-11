package ru.lonelywh1te.aquaup.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.lonelywh1te.aquaup.domain.usecase.GetTodayWaterLogsUseCase

class HomeViewModel(
    private val getTodayWaterLogsUseCase: GetTodayWaterLogsUseCase,
): ViewModel() {
    val state: StateFlow<HomeScreenState> = getTodayWaterLogsUseCase()
        .map {
            // TODO

            HomeScreenState.Success.getPreviewState()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeScreenState.Loading
        )

}