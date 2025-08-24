package ru.lonelywh1te.aquaup.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.lonelywh1te.aquaup.presentation.history.HistoryViewModel
import ru.lonelywh1te.aquaup.presentation.home.HomeViewModel
import ru.lonelywh1te.aquaup.presentation.main.MainViewModel
import ru.lonelywh1te.aquaup.presentation.notification.AppNotificationManager
import ru.lonelywh1te.aquaup.presentation.notification.content.WaterNotificationContent
import ru.lonelywh1te.aquaup.presentation.reminder.WaterReminder
import ru.lonelywh1te.aquaup.presentation.settings.SettingsViewModel
import ru.lonelywh1te.aquaup.presentation.worker.WaterReminderWorker

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
            getWaterLogsByDateUseCase = get(),
            updateWaterLogUseCase = get(),
            deleteWaterLogUseCase = get(),
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            settingsPreferences = get(),
            waterReminder = get(),
        )
    }

    viewModel<MainViewModel> {
        MainViewModel(
            settingsPreferences = get(),
            getWaterLogsByDateUseCase = get(),
        )
    }

    single<AppNotificationManager> {
        AppNotificationManager(
            context = androidContext(),
            waterNotificationContent = get()
        )
    }

    factory<WaterNotificationContent> {
        WaterNotificationContent(androidContext())
    }

    single<WaterReminder> {
        WaterReminder(androidContext())
    }

    worker<WaterReminderWorker> {
        WaterReminderWorker(
            context = androidContext(),
            workerParameters = get(),
            notificationManager = get(),
        )
    }

}