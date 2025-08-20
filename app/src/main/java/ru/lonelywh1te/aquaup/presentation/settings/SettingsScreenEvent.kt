package ru.lonelywh1te.aquaup.presentation.settings

import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import java.time.LocalTime

sealed class SettingsScreenEvent {
    data class VolumeUnitChanged(val value: VolumeUnit): SettingsScreenEvent()
    data class ThemeChanged(val value: AppTheme): SettingsScreenEvent()
    data class ReminderScheduleChanged(val value: ReminderSchedule): SettingsScreenEvent()
    data class ReminderTimesChanged(val value: List<LocalTime>): SettingsScreenEvent()
    data class WaterGoalChanged(val value: WaterGoal): SettingsScreenEvent()
}