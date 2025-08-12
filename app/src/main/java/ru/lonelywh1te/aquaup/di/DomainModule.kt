package ru.lonelywh1te.aquaup.di

import org.koin.dsl.module
import ru.lonelywh1te.aquaup.data.WaterLogRepositoryImpl
import ru.lonelywh1te.aquaup.data.source.prefs.impl.SettingsPreferencesImpl
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.usecase.AddWaterUseCase
import ru.lonelywh1te.aquaup.domain.usecase.GetTodayWaterLogsUseCase

val domainModule = module {

    single<SettingsPreferences> {
        SettingsPreferencesImpl(get())
    }

    single<WaterLogRepository> {
        WaterLogRepositoryImpl(
            waterLogDao = get()
        )
    }

    factory<GetTodayWaterLogsUseCase> {
        GetTodayWaterLogsUseCase(
            waterLogRepository = get()
        )
    }

    factory<AddWaterUseCase> {
        AddWaterUseCase(
            waterLogRepository = get()
        )
    }

}