package ru.lonelywh1te.aquaup.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.lonelywh1te.aquaup.R

class AppNotificationManager(
    private val context: Context,
) {
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun initChannels() {
        notificationManager.createNotificationChannels(
            AquaUpNotificationChannels.entries.map {
                NotificationChannel(it.id, it.channelName, it.importance)
            }
        )
    }

    fun showWaterReminderNotification() {
        val notification = NotificationCompat.Builder(context, AquaUpNotificationChannels.WaterRemindChannel.id)
            .setContentTitle(context.getString(R.string.water_remind_notification_title1))
            .setContentText(context.getString(R.string.water_remind_notification_description1))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notification)
    }

    fun cancelWaterReminderNotification() {
        notificationManager.cancel(WATER_REMINDER_NOTIFICATION_ID)
    }

    companion object {
        private const val WATER_REMINDER_NOTIFICATION_ID = 1
    }
}