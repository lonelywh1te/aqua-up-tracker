package ru.lonelywh1te.aquaup.presentation.notification.content

import android.content.Context
import ru.lonelywh1te.aquaup.R

class WaterNotificationContent(
    context: Context,
) {
    private val titles: Array<String> = context.resources.getStringArray(R.array.water_reminder_titles)
    private val messages: Array<String> = context.resources.getStringArray(R.array.water_reminder_messages)

    fun getRandomTitle(): String {
        return titles.random()
    }

    fun getRandomMessage(): String {
        return messages.random()
    }
}