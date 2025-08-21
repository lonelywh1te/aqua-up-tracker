package ru.lonelywh1te.aquaup

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.lonelywh1te.aquaup.di.dataModule
import ru.lonelywh1te.aquaup.di.domainModule
import ru.lonelywh1te.aquaup.di.presentationModule
import ru.lonelywh1te.aquaup.presentation.notification.AppNotificationManager

class App: Application() {

    private val notificationManager: AppNotificationManager by inject<AppNotificationManager>()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            androidLogger(level = Level.DEBUG)
            workManagerFactory()
            modules(
                dataModule,
                domainModule,
                presentationModule,
            )
        }

        notificationManager.initChannels()
    }
}