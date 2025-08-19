package ru.lonelywh1te.aquaup.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.lonelywh1te.aquaup.presentation.history.HistoryViewModel
import ru.lonelywh1te.aquaup.presentation.home.HomeViewModel
import ru.lonelywh1te.aquaup.presentation.settings.SettingsViewModel

val presentationModule = module {

    viewModel<HomeViewModel> {
        HomeViewModel(
            settingsPreferences = get(),
            getTodayWaterLogsUseCase = get(),
            addWaterUseCase = get(),
        )
    }

    viewModel<HistoryViewModel> {
        HistoryViewModel(
            settingsPreferences = get(),
            getTodayWaterLogsUseCase = get(),
            updateWaterLogUseCase = get(),
            deleteWaterLogUseCase = get(),
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            settingsPreferences = get(),
        )
    }

}