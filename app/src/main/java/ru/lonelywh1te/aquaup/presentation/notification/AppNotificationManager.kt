package ru.lonelywh1te.aquaup.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.PendingIntentCompat
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.presentation.MainActivity
import ru.lonelywh1te.aquaup.presentation.notification.content.WaterNotificationContent

class AppNotificationManager(
    private val context: Context,
    private val waterNotificationContent: WaterNotificationContent,
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
        val intent = Intent(context, MainActivity::class.java)
        val notifyPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, AquaUpNotificationChannels.WaterRemindChannel.id)
            .setContentTitle(waterNotificationContent.getRandomTitle())
            .setContentText(waterNotificationContent.getRandomMessage())
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(notifyPendingIntent)
            .setAutoCancel(true)
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