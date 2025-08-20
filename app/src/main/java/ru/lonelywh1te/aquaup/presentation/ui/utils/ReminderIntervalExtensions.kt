package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval

@StringRes
fun ReminderInterval.valueStringRes(): Int {
    return when (this) {
        ReminderInterval.None -> R.string.dont_remind
        ReminderInterval.EveryOneHour -> R.string.every_hour
        ReminderInterval.EveryTwoHours -> R.string.every_two_hours
        ReminderInterval.EveryThreeHours -> R.string.every_three_hours
        ReminderInterval.Custom -> R.string.custom_remind
    }
}