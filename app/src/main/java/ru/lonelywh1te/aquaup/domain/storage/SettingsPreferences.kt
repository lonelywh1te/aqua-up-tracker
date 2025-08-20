package ru.lonelywh1te.aquaup.domain.storage

import kotlinx.coroutines.flow.Flow
import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal

interface SettingsPreferences {
    val volumeUnitFlow: Flow<VolumeUnit>
    val waterGoalFlow: Flow<WaterGoal>
    val themeFlow: Flow<AppTheme>
    val reminderSchedule: Flow<ReminderSchedule>

    suspend fun setVolumeUnit(value: VolumeUnit)
    suspend fun setWaterGoal(value: WaterGoal)
    suspend fun setTheme(value: AppTheme)
    suspend fun setReminderSchedule(value: ReminderSchedule)

}