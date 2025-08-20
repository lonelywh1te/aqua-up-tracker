package ru.lonelywh1te.aquaup.presentation.navigation

sealed class SettingsDestination {
    data object VolumeUnit: SettingsDestination()
    data class WaterGoal(val value: Int): SettingsDestination()
    data object AppTheme: SettingsDestination()
    data object ReminderSchedule: SettingsDestination()
    data object ReminderTimes: SettingsDestination()
}