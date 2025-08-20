package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.SettingsItem

@StringRes
fun SettingsItem.stringRes(): Int {
    return when (this) {
        is SettingsItem.AppTheme -> R.string.app_theme
        is SettingsItem.ReminderInterval -> R.string.reminder
        is SettingsItem.VolumeUnit -> R.string.volume_unit
        is SettingsItem.WaterGoal -> R.string.water_goal
    }
}