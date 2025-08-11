package ru.lonelywh1te.aquaup.di

import org.koin.dsl.module
import ru.lonelywh1te.aquaup.data.source.db.MainDatabase
import ru.lonelywh1te.aquaup.data.source.db.water_log.WaterLogDao

val dataModule = module {

    single<MainDatabase> {
        MainDatabase.instance(get())
    }

    single<WaterLogDao> {
        get<MainDatabase>().waterLogDao()
    }

}