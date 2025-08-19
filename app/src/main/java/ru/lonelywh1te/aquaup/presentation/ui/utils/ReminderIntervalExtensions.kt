package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval

@StringRes
fun ReminderInterval.stringRes(): Int {
    return when (this) {
        is ReminderInterval.None -> R.string.dont_remind
        is ReminderInterval.OneHour -> R.string.every_hour
        is ReminderInterval.TwoHours -> R.string.every_two_hours
        is ReminderInterval.ThreeHours -> R.string.every_three_hours
        is ReminderInterval.Custom -> R.string.custom_remind
    }
}