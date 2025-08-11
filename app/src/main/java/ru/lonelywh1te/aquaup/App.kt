package ru.lonelywh1te.aquaup

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.lonelywh1te.aquaup.di.dataModule
import ru.lonelywh1te.aquaup.di.domainModule
import ru.lonelywh1te.aquaup.di.presentationModule

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            androidLogger(level = Level.DEBUG)
            modules(
                dataModule,
                domainModule,
                presentationModule,
            )
        }
    }
}