package ru.lonelywh1te.aquaup.presentation.settings

import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal

sealed class SettingsScreenEvent {
    data class VolumeUnitChanged(val value: VolumeUnit): SettingsScreenEvent()
    data class ThemeChanged(val value: AppTheme): SettingsScreenEvent()
    data class ReminderIntervalChanged(val value: ReminderInterval): SettingsScreenEvent()
    data class WaterGoalChanged(val value: WaterGoal): SettingsScreenEvent()
}