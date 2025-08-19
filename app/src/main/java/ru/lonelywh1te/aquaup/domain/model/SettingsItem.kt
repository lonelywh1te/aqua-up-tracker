package ru.lonelywh1te.aquaup.domain.model

sealed class SettingsItem {
    data object VolumeUnit: SettingsItem()
    data object WaterGoal: SettingsItem()
    data object AppTheme: SettingsItem()
    data object ReminderInterval: SettingsItem()
}