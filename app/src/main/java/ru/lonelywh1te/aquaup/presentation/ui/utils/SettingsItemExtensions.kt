package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.presentation.navigation.SettingsDestination

@StringRes
fun SettingsDestination.stringRes(): Int {
    return when (this) {
        is SettingsDestination.AppTheme -> R.string.app_theme
        is SettingsDestination.ReminderSchedule -> R.string.reminder
        is SettingsDestination.VolumeUnit -> R.string.volume_unit
        is SettingsDestination.WaterGoal -> R.string.water_goal
        is SettingsDestination.ReminderTimes -> R.string.reminder_times
    }
}