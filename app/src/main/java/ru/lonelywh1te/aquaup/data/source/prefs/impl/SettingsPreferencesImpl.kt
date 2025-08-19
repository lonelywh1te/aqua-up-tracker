package ru.lonelywh1te.aquaup.data.source.prefs.impl

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ru.lonelywh1te.aquaup.data.toLocalTimeList
import ru.lonelywh1te.aquaup.data.toPrefsString
import ru.lonelywh1te.aquaup.domain.model.settings.AppTheme
import ru.lonelywh1te.aquaup.domain.model.settings.ReminderInterval
import ru.lonelywh1te.aquaup.domain.storage.SettingsPreferences
import ru.lonelywh1te.aquaup.domain.model.settings.VolumeUnit
import ru.lonelywh1te.aquaup.domain.model.settings.WaterGoal
import ru.lonelywh1te.aquaup.domain.model.settings.name

class SettingsPreferencesImpl(
    private val context: Context,
): SettingsPreferences {
    val prefs: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override var volumeUnit: VolumeUnit
        get() = VolumeUnit.valueOf(prefs.getString(VOLUME_UNIT_KEY, VolumeUnit.Ml.name)!!)
        set(value) = prefs.edit {
            putString(VOLUME_UNIT_KEY, value.name)
        }

    override var waterGoal: WaterGoal
        get() = WaterGoal(prefs.getInt(WATER_GOAL_KEY, 0))
        set(it) = prefs.edit {
            putInt(WATER_GOAL_KEY, it.value)
        }

    override var theme: AppTheme
        get() = AppTheme.valueOf(prefs.getString(THEME_KEY, AppTheme.Light.name)!!)
        set(value) = prefs.edit {
            putString(THEME_KEY, value.name)
        }

    override var reminderInterval: ReminderInterval
        get() = ReminderInterval.fromName(
            name = prefs.getString(REMINDER_INTERVAL, ReminderInterval.None.name())!!,
            times = prefs.getString(REMINDER_INTERVAL_TIMES, null)?.toLocalTimeList() ?: emptyList()
        )
        set(value) = prefs.edit {
            putString(REMINDER_INTERVAL, value.name())

            if (value is ReminderInterval.Custom && value.times.isNotEmpty()) {
                putString(REMINDER_INTERVAL_TIMES, value.times.toPrefsString())
            }
        }


    companion object {
        private const val NAME = "settings"
        private const val VOLUME_UNIT_KEY = "volume_unit"
        private const val WATER_GOAL_KEY = "water_goal"
        private const val THEME_KEY = "theme"
        private const val REMINDER_INTERVAL = "reminder_interval"
        private const val REMINDER_INTERVAL_TIMES = "reminder_interval_times"
    }
}