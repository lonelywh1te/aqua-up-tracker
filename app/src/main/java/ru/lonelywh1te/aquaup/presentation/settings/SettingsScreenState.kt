package ru.lonelywh1te.aquaup.presentation.settings

import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal

sealed class SettingsScreenState {
    data class Success (
        val volumeUnit: VolumeUnit,
        val waterGoal: WaterGoal,
        val theme: AppTheme,
        val reminderSchedule: ReminderSchedule,

        ): SettingsScreenState() {
        companion object {
            val preview: SettingsScreenState.Success = SettingsScreenState.Success(
                volumeUnit = VolumeUnit.Ml,
                waterGoal = WaterGoal(1800),
                theme = AppTheme.System,
                reminderSchedule = ReminderSchedule(ReminderInterval.Custom, emptyList())
            )
        }
    }

    data object Loading: SettingsScreenState()

    data class Error(val message: String): SettingsScreenState()
}
