package ru.lonelywh1te.aquaup.presentation.settings

import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.presentation.home.HomeScreenState

sealed class SettingsScreenState {
    data class Success (
        val volumeUnit: VolumeUnit,
        val waterGoal: WaterGoal,
        val theme: AppTheme,
        val reminderInterval: ReminderInterval,

        ): SettingsScreenState() {
        companion object {
            val preview: SettingsScreenState.Success = SettingsScreenState.Success(
                volumeUnit = VolumeUnit.Ml,
                waterGoal = WaterGoal(1800),
                theme = AppTheme.System,
                reminderInterval = ReminderInterval.OneHour
            )
        }
    }

    data object Loading: SettingsScreenState()

    data class Error(val message: String): SettingsScreenState()
}
