package ru.lonelywh1te.aquaup.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.lonelywh1te.aquaup.presentation.home.HomeViewModel

val presentationModule = module {

    viewModel<HomeViewModel> {
        HomeViewModel(
            settingsPreferences = get(),
            getTodayWaterLogsUseCase = get(),
            addWaterUseCase = get(),
        )
    }

}