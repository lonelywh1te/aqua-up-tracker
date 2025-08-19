package ru.lonelywh1te.aquaup.data.source.prefs.impl

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.lonelywh1te.aquaup.data.toLocalTimeList
import ru.lonelywh1te.aquaup.data.toPrefsString
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.domain.model.settings.name
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
        val reminderIntervalTimes = stringPreferencesKey("reminder_interval_times")
    }

    private val dataStore = context.dataStore

    override val waterGoalFlow: Flow<WaterGoal> = dataStore.data.map { prefs ->
        val value = prefs[PreferencesKeys.waterGoal] ?: 0
        WaterGoal(value)
    }

    override val volumeUnitFlow: Flow<VolumeUnit> = dataStore.data.map { prefs ->
        val volumeUnitName = prefs[PreferencesKeys.volumeUnit] ?: VolumeUnit.Ml.name
        VolumeUnit.valueOf(volumeUnitName)
    }

    override val reminderIntervalFlow: Flow<ReminderInterval> = dataStore.data.map { prefs ->
        val reminderIntervalName = prefs[PreferencesKeys.reminderInterval] ?: ReminderInterval.None.name()
        var reminderInterval = ReminderInterval.fromName(reminderIntervalName)

        if (reminderInterval is ReminderInterval.Custom) {
            reminderInterval = reminderInterval.copy(times = prefs[PreferencesKeys.reminderIntervalTimes]?.toLocalTimeList() ?: emptyList())
        }

        reminderInterval
    }

    override val themeFlow: Flow<AppTheme> = dataStore.data.map { prefs ->
        val themeName = prefs[PreferencesKeys.theme] ?: AppTheme.Light.name
        AppTheme.valueOf(themeName)
    }


    override suspend fun setVolumeUnit(value: VolumeUnit) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.volumeUnit] = value.name
        }
    }

    override suspend fun setWaterGoal(value: WaterGoal) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.waterGoal] = value.value
        }
    }

    override suspend fun setTheme(value: AppTheme) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.theme] = value.name
        }
    }

    override suspend fun setReminderInterval(value: ReminderInterval) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.reminderInterval] = value.name()

            if (value is ReminderInterval.Custom) {
                settings[PreferencesKeys.reminderIntervalTimes] = value.times.toPrefsString()
            }
        }
    }

    companion object {
        const val NAME = "settings"

    }
}