package ru.lonelywh1te.aquaup.di

import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.dsl.module

val coreModule = module {

    single<FirebaseCrashlytics> {
        FirebaseCrashlytics.getInstance()
    }

}