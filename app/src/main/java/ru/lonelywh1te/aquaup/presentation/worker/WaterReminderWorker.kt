package ru.lonelywh1te.aquaup.presentation.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import ru.lonelywh1te.aquaup.presentation.notification.AppNotificationManager

class WaterReminderWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val notificationManager: AppNotificationManager,
): Worker(context, workerParameters), KoinComponent {


    override fun doWork(): Result {
        notificationManager.showWaterReminderNotification()
        return Result.success()
    }

}