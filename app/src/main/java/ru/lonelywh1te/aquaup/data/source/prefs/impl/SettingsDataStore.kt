package ru.lonelywh1te.aquaup.data.source.prefs.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.lonelywh1te.aquaup.data.toLocalTimeList
import ru.lonelywh1te.aquaup.data.toPrefsString
import ru.lonelywh1te.aquaup.domain.model.ReminderSchedule
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.domain.model.settings.convertMlToOz
import ru.lonelywh1te.aquaup.domain.model.settings.convertOzToMl
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SettingsDataStore.NAME)

class SettingsDataStore(
    context: Context,
): SettingsPreferences {
    private object PreferencesKeys {
        val waterGoal = intPreferencesKey("water_goal")
        val volumeUnit = stringPreferencesKey("volume_unit")
        val theme = stringPreferencesKey("theme")

        val reminderInterval = stringPreferencesKey("reminder_interval")
        val reminderTimes = stringPreferencesKey("reminder_times")
    }

    private val dataStore = context.dataStore

    override val waterGoalFlow: Flow<WaterGoal> = dataStore.data.map { prefs ->
        val volumeUnit = volumeUnitFlow.first()
        val value = prefs[PreferencesKeys.waterGoal] ?: 0

        WaterGoal(
            when (volumeUnit) {
                VolumeUnit.Ml -> value
                VolumeUnit.Oz -> convertMlToOz(value)
            }
        )
    }

    override val volumeUnitFlow: Flow<VolumeUnit> = dataStore.data.map { prefs ->
        val volumeUnitName = prefs[PreferencesKeys.volumeUnit] ?: VolumeUnit.Ml.name
        VolumeUnit.valueOf(volumeUnitName)
    }

    override val reminderSchedule: Flow<ReminderSchedule> = dataStore.data.map { prefs ->
        val reminderIntervalName = prefs[PreferencesKeys.reminderInterval] ?: ReminderInterval.None.name

        val reminderInterval = ReminderInterval.valueOf(reminderIntervalName)
        val reminderTimes = prefs[PreferencesKeys.reminderTimes]?.toLocalTimeList() ?: emptyList()

        ReminderSchedule(reminderInterval, reminderTimes)
    }

    override val themeFlow: Flow<AppTheme> = dataStore.data.map { prefs ->
        val themeName = prefs[PreferencesKeys.theme] ?: AppTheme.System.name
        AppTheme.valueOf(themeName)
    }

    override suspend fun setVolumeUnit(value: VolumeUnit) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.volumeUnit] = value.name
        }
    }

    override suspend fun setWaterGoal(value: WaterGoal) {
        val volumeUnit = volumeUnitFlow.first()

        dataStore.edit { settings ->
            settings[PreferencesKeys.waterGoal] = when (volumeUnit) {
                VolumeUnit.Ml -> value.value
                VolumeUnit.Oz -> convertOzToMl(value.value)
            }
        }
    }

    override suspend fun setTheme(value: AppTheme) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.theme] = value.name
        }
    }

    override suspend fun setReminderSchedule(value: ReminderSchedule) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.reminderInterval] = value.interval.name
            settings[PreferencesKeys.reminderTimes] = value.times.toPrefsString()
        }
    }

    companion object {
        const val NAME = "settings"

    }
}