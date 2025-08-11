package ru.lonelywh1te.aquaup.di

import org.koin.dsl.module
import ru.lonelywh1te.aquaup.data.WaterLogRepositoryImpl
import ru.lonelywh1te.aquaup.domain.repository.WaterLogRepository
import ru.lonelywh1te.aquaup.domain.usecase.GetTodayWaterLogsUseCase

val domainModule = module {

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

}