package ru.lonelywh1te.aquaup.domain.model

sealed class SettingsItem {
    data object VolumeUnit: SettingsItem()
    data class WaterGoal(val value: Int): SettingsItem()
    data object AppTheme: SettingsItem()
    data object ReminderInterval: SettingsItem()
}