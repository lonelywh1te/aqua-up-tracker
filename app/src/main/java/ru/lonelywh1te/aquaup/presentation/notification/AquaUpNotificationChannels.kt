package ru.lonelywh1te.aquaup.presentation.notification

import android.app.NotificationManager

enum class AquaUpNotificationChannels(
    val id: String,
    val channelName: String,
    val importance: Int,
) {
    WaterRemindChannel(
        id = "task_reminder",
        channelName = "Напоминание о воде",
        importance = NotificationManager.IMPORTANCE_HIGH
    )
}