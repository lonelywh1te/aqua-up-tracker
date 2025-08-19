package ru.lonelywh1te.aquaup.presentation.ui.utils

import androidx.annotation.StringRes
import ru.lonelywh1te.aquaup.R
import ru.lonelywh1te.aquaup.domain.model.SettingsItem

@StringRes
fun SettingsItem.stringRes(): Int {
    return when (this) {
        SettingsItem.AppTheme -> R.string.app_theme
        SettingsItem.ReminderInterval -> R.string.reminder
        SettingsItem.VolumeUnit -> R.string.volume_unit
        SettingsItem.WaterGoal -> R.string.water_goal
    }
}