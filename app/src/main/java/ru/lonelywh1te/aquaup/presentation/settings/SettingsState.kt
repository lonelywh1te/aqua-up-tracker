package ru.lonelywh1te.aquaup.presentation.settings

import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal

data class SettingsState (
    val volumeUnit: VolumeUnit,
    val waterGoal: WaterGoal,
    val theme: AppTheme,
    val reminderInterval: ReminderInterval,
)